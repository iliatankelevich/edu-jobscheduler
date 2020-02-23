package edu.ilia.jobsystem.service;

import edu.ilia.jobssystem.api.JobsService;
import edu.ilia.jobssystem.models.ExecutionMode;
import edu.ilia.jobssystem.models.Job;
import edu.ilia.jobssystem.models.JobServiceResponse;
import edu.ilia.jobsystem.service.dao.JobsDao;
import edu.ilia.jobsystem.service.queue.QueueManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author ilia.tankelevich
 * @date 21/02/2020
 */
@Service
public class JobsServiceImpl implements JobsService {
    @Autowired
    private QueueManager queue;

    @Autowired
    private JobsDao dao;

    @Autowired
    private JobHandler jobHandler;

    @Override
    public JobServiceResponse submitJob(Job job) {
        job.setStatus("pending");
        JobServiceResponse result;
        result = dao.insertJob(job);
        if(ExecutionMode.NOW == job.getExecutionMode()){
            queue.push(result.getId());
        }

        return result;
    }

    @Override
    public boolean deleteJob(int jobId) {
        return dao.deleteJob(jobId);
    }

    @Override
    public List<JobServiceResponse> getJobs(List<Integer> jobIds) {
        return dao.getJobs(jobIds);
    }

    @Override
    public Set<String> getSupportedJobTypes() {
        return jobHandler.getSupportedJobTypes();
    }
}
