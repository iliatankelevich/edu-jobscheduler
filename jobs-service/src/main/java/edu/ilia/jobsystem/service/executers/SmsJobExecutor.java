package edu.ilia.jobsystem.service.executers;

import edu.ilia.jobssystem.models.Job;
import edu.ilia.jobssystem.models.JobServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author ilia.tankelevich
 * @date 21/02/2020
 */

@Component("sms")
public class SmsJobExecutor implements JobExecutor {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Override
    public JobServiceResponse execute(Job job) {
        log.info("job " + job.getId() + " executed");
        return job;
    }
}
