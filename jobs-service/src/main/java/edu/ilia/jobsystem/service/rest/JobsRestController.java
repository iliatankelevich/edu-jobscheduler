package edu.ilia.jobsystem.service.rest;

import edu.ilia.jobssystem.api.JobsService;
import edu.ilia.jobssystem.models.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ilia.tankelevich
 * @date 21/02/2020
 */
@CrossOrigin
@RestController
@RequestMapping("/api/jobs")
public class JobsRestController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private JobsService jobsService;

    @RequestMapping(path = "/test", method= RequestMethod.GET)
    public @ResponseBody ResponseEntity test(){

        return ResponseEntity.ok("test");
    }

    @RequestMapping(path = "/submit", method= RequestMethod.POST)
    public @ResponseBody ResponseEntity submitJob(@RequestBody Job job) {
        return ResponseEntity.ok(jobsService.submitJob(job));
    }

    @RequestMapping(path = "/delete", method= RequestMethod.DELETE)
    public @ResponseBody ResponseEntity deleteJob(@RequestParam("job_id") int jobId) {
        if(jobsService.deleteJob(jobId)){
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(method= RequestMethod.POST)
    public @ResponseBody ResponseEntity getJobs(@RequestBody List<Integer> jobIds) {
        return ResponseEntity.ok(jobsService.getJobs(jobIds));
    }

    @RequestMapping(path = "/supported_types", method= RequestMethod.GET)
    public @ResponseBody ResponseEntity getSupportedJobTypes() {
        return ResponseEntity.ok(jobsService.getSupportedJobTypes());
    }
}
