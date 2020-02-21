package edu.ilia.jobsystem.service.rest;

import edu.ilia.jobssystem.api.JobsController;
import edu.ilia.jobssystem.models.ExecutionTime;
import edu.ilia.jobssystem.models.Job;
import edu.ilia.jobssystem.models.JobServiceResponse;
import edu.ilia.jobsystem.service.JobHandler;
import edu.ilia.jobsystem.service.executers.JobExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author ilia.tankelevich
 * @date 21/02/2020
 */
@CrossOrigin
@RestController
@RequestMapping("/jobs/api")
public class JobsControllerImpl implements JobsController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private JobHandler jobHandler;

    @RequestMapping(path = "/test", method= RequestMethod.GET)
    public @ResponseBody String test(){

        return "test";
    }
    
    @Override
    public JobServiceResponse submitJob(Job job) {
        JobServiceResponse result;
        //TODO: store job in the DB
        if(ExecutionTime.NOW == job.getExecutionTime()){
            result = jobHandler.handle(job);
        }else{
            result = job;
        }

        return result;
    }

    @Override
    public JobServiceResponse deleteJob(int jobId) {
        return null;
    }

    @Override
    public JobServiceResponse getJobs(List<Integer> jobIds) {
        return null;
    }
}
