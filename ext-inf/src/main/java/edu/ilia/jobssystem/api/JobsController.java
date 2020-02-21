package edu.ilia.jobssystem.api;

import edu.ilia.jobssystem.models.Job;
import edu.ilia.jobssystem.models.JobServiceResponse;

import java.util.List;

/**
 * @author ilia.tankelevich
 * @date 21/02/2020
 */
public interface JobsController {
    JobServiceResponse submitJob(Job job);
    JobServiceResponse deleteJob(int jobId);
    JobServiceResponse getJobs(List<Integer> jobIds);
}
