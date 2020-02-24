package edu.ilia.jobsystem.service;

import edu.ilia.jobssystem.models.ExecutionMode;
import edu.ilia.jobsystem.service.dao.JobsDao;
import edu.ilia.jobsystem.service.queue.QueueManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ilia.tankelevich
 * @date 22/02/2020
 */
@Service
public class Scheduler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private QueueManager queue;

    @Autowired
    private JobsDao dao;

    @Retryable
    @Scheduled(cron = "0 0 * * * *")
    public void everyHourScheduler(){
        log.info("'every hour' scheduler running");
        pushToQueueJobsByType(ExecutionMode.EVERY_HOUR);
    }

    @Retryable
    @Scheduled(cron = "0 0 */2 * * *")
    public void every2HoursScheduler(){
        log.info("'every 2 hour' scheduler running");
        pushToQueueJobsByType(ExecutionMode.EVERY_2_HOURS);
    }

    @Retryable
    @Scheduled(cron = "0 0 */6 * * *")
    public void every6HoursScheduler(){
        log.info("'every 6 hour' scheduler running");
        pushToQueueJobsByType(ExecutionMode.EVERY_6_HOURS);
    }

    @Retryable
    @Scheduled(cron = "0 0 */12 * * *")
    public void every12HoursScheduler(){
        log.info("'every 12 hour' scheduler running");
        pushToQueueJobsByType(ExecutionMode.EVERY_12_HOURS);
    }

    private void pushToQueueJobsByType(ExecutionMode executionMode){
        List<Integer> jobIds = dao.getJobIdsByExecutionModeAndStatus(executionMode, "pending");
        queue.push(jobIds);
    }
}
