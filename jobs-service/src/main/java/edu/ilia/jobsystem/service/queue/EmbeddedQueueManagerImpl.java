package edu.ilia.jobsystem.service.queue;

import edu.ilia.jobssystem.models.Job;
import edu.ilia.jobssystem.models.JobServiceResponse;
import edu.ilia.jobsystem.service.JobHandler;
import edu.ilia.jobsystem.service.dao.JobsDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.Session;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ilia.tankelevich
 * @date 22/02/2020
 */

@Component
public class EmbeddedQueueManagerImpl extends QueueManagerImpl {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private JobHandler jobHandler;

    @Autowired
    private JobsDao dao;

    @Override
    void pushToQueue(@NotNull List<Integer> jobIds) {
        jobIds.forEach(jobId -> jmsTemplate.convertAndSend("jobs", jobId));
    }

    @Override
    List<Integer> popFromQueue() {
        return null;
    }

    @Override
    public int getSizeOfQueue() {
        return 0;
    }

    @JmsListener(destination = "jobs")
    public void receiveMessage(@Payload Integer jobId) {
        log.info("received job id: " + jobId + " from queue");
        JobServiceResponse jobServiceResponse = dao.getJob(jobId);
        if (jobServiceResponse instanceof Job){
            jobHandler.handle((Job)jobServiceResponse);
        }
    }
}
