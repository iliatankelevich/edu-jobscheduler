package edu.ilia.jobsystem.service.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author ilia.tankelevich
 * @date 22/02/2020
 */
public abstract class QueueManagerImpl implements QueueManager {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    abstract void pushToQueue(List<Integer> jobIds);
    abstract List<Integer> popFromQueue();

    @Override
    public final void push(int jobId) {
        log.info("pushing job " + jobId + " to queue");
        pushToQueue(Arrays.asList(jobId));
    }

    @Override
    public final void push(List<Integer> jobIds) {
        log.info("pushing jobs " + StringUtils.collectionToCommaDelimitedString(jobIds) + " to queue");
        pushToQueue(jobIds);
    }

    @Override
    public final List<Integer> pop() {
        return popFromQueue();
    }
}
