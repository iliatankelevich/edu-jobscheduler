package edu.ilia.jobsystem.service.dao;

import edu.ilia.jobssystem.models.ExecutionMode;
import edu.ilia.jobssystem.models.Job;
import edu.ilia.jobssystem.models.JobServiceResponse;

import java.util.List;

/**
 * @author ilia.tankelevich
 * @date 21/02/2020
 */
public interface JobsDao {
    void initDB();
    Job insertJob(Job job);
    boolean updateJobStatus(int id, String started);

    List<Integer> getJobIdsByExecutionMode(ExecutionMode every2Hours);

    JobServiceResponse getJob(Integer jobId);

    boolean deleteJob(int jobId);

    List<JobServiceResponse> getJobs(List<Integer> jobIds);
}
