package edu.ilia.jobsystem.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import edu.ilia.jobssystem.models.ExecutionMode;
import edu.ilia.jobssystem.models.Job;
import edu.ilia.jobssystem.models.JobServiceResponse;
import edu.ilia.jobsystem.service.dao.JobsDao;
import edu.ilia.jobsystem.service.executers.JobExecutor;
import edu.ilia.jobsystem.service.executers.SmsJobExecutorImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ilia.tankelevich
 * @date 23/02/2020
 */
@RunWith(MockitoJUnitRunner.class)
public class JobHandlerTest {
    @Mock
    private JobsDao dao;


    @InjectMocks
    private JobHandler jobHandler;

    @Before
    public void init() throws NoSuchFieldException, IllegalAccessException {
        Map<String, JobExecutor> executors = new HashMap<>();
        executors.put("sms", new SmsJobExecutorImpl());
        Field executorsField = jobHandler.getClass().getDeclaredField("executors");
        executorsField.setAccessible(true);
        executorsField.set(jobHandler, executors);
    }

    @Test
    public void handleTest(){
        Job job = new Job();
        job.setId(1);
        job.setName("test");
        job.setStatus("test");
        job.setExecutionMode(ExecutionMode.NOW);
        job.setJobType("sms");
        JobServiceResponse response = jobHandler.handle(job);
        assertNotNull(response);
    }
}
