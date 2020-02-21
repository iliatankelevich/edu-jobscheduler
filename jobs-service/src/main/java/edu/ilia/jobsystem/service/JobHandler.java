package edu.ilia.jobsystem.service;

import edu.ilia.jobssystem.models.Job;
import edu.ilia.jobssystem.models.JobServiceError;
import edu.ilia.jobssystem.models.JobServiceResponse;
import edu.ilia.jobsystem.service.executers.JobExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public JobHandler(List<JobExecutor> executorsList) {
        executors = executorsList.stream().collect(Collectors.toMap(JobExecutor::getName, jobExecutor -> jobExecutor));
    }

    public JobServiceResponse handle(Job job) {
        JobServiceResponse result = null;
        String errMsg = null;
        if(executors.containsKey(job.getJobType())){
            JobExecutor jobExecutor = executors.get(job.getJobType());
            try{
                result = jobExecutor.execute(job);
            }catch(Exception ex){
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
        }

        return result;
    }
}
