package edu.ilia.jobsystem.service;

import edu.ilia.jobssystem.models.Job;
import edu.ilia.jobssystem.models.JobServiceError;
import edu.ilia.jobssystem.models.JobServiceResponse;
import edu.ilia.jobsystem.service.dao.JobsDao;
import edu.ilia.jobsystem.service.executers.JobExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ilia.tankelevich
 * @date 21/02/2020
 */
@Primary
@Service
public class JobHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Map<String, JobExecutor> executors;

    private final JobsDao dao;

    public JobHandler(List<JobExecutor> executorsList, JobsDao dao) {
        if (executorsList != null){
            executors = executorsList.stream().collect(Collectors.toMap(JobExecutor::getName, jobExecutor -> jobExecutor));
        }else{
            executors = new HashMap<>();
        }

        this.dao = dao;
    }

    public JobServiceResponse handle(Job job) {
        JobServiceResponse result = null;
        if(job != null){
            dao.updateJobStatus(job.getId(), "running");
            String errMsg = null;
            if(executors.containsKey(job.getJobType())){
                JobExecutor jobExecutor = executors.get(job.getJobType());
                try{
                    result = jobExecutor.execute(job);
                }catch(Exception ex){
                    dao.updateJobStatus(job.getId(), "failed");
                    job.setStatus("failed");
                    errMsg = "failed to execute job of type: " + job.getJobType();
                }finally {
                    try{
                        jobExecutor.cleanup();
                    }catch(Exception cleanupException){
                        log.error("failed to clean up after job type " + job.getJobType());
                    }
                }
            }else{
                errMsg = "no executor found for job type: " + job.getJobType();
            }

            if(errMsg != null){
                log.error(errMsg);
                throw new RuntimeException(errMsg);
            }else{
                dao.updateJobStatus(job.getId(), "done");
                job.setStatus("done");
            }
        }

        return result;
    }

    public Set<String> getSupportedJobTypes() {
        return executors.keySet();
    }
}