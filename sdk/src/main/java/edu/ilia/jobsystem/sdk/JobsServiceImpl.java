package edu.ilia.jobsystem.sdk;

import edu.ilia.jobssystem.api.JobsService;
import edu.ilia.jobssystem.models.Job;
import edu.ilia.jobssystem.models.JobServiceResponse;
import edu.ilia.jobsystem.sdk.utils.RestClient;
import edu.ilia.jobsystem.sdk.utils.RestClientImpl;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Set;

/**
 * @author ilia.tankelevich
 * @date 23/02/2020
 */
public class JobsServiceImpl implements JobsService {
    private RestClient restClient;

    public JobsServiceImpl(String serviceBaseUrl) {
        restClient = new RestClientImpl();
        restClient.setApiHost(serviceBaseUrl);
    }

    @Override
    public JobServiceResponse submitJob(Job job) {
        return restClient.invokeService(new ParameterizedTypeReference<JobServiceResponse>() {}, "/submit", job, HttpMethod.POST);
    }

    @Override
    public boolean deleteJob(int jobId) {
        return restClient.invokeService(new ParameterizedTypeReference<Boolean>() {}, "/delete?job_id="+jobId, null, HttpMethod.DELETE);
    }

    @Override
    public List<JobServiceResponse> getJobs(List<Integer> jobIds) {
        return restClient.invokeService(new ParameterizedTypeReference<List<JobServiceResponse>>() {}, "", jobIds, HttpMethod.POST);
    }

    @Override
    public Set<String> getSupportedJobTypes() {
        return restClient.invokeService(new ParameterizedTypeReference<Set<String>>() {}, "/supported_types", null, HttpMethod.GET);
    }
}
