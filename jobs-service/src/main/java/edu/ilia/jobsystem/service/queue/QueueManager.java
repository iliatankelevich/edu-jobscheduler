package edu.ilia.jobsystem.service.queue;

import java.util.List;

/**
 * @author ilia.tankelevich
 * @date 22/02/2020
 */
public interface QueueManager {
    void push(int jobId);
    void push(List<Integer> jobIds);
    List<Integer> pop();
    int getSizeOfQueue();
}
