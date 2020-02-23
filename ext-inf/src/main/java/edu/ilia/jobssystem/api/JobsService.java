package edu.ilia.jobssystem.api;

import edu.ilia.jobssystem.models.Job;
import edu.ilia.jobssystem.models.JobServiceResponse;

import java.util.List;
import java.util.Set;

/**
 * @author ilia.tankelevich
 * @date 21/02/2020
 */
public interface JobsService {
    JobServiceResponse submitJob(Job job);
    boolean deleteJob(int jobId);
    List<JobServiceResponse> getJobs(List<Integer> jobIds);
    Set<String> getSupportedJobTypes();
}
