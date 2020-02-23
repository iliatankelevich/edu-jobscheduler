package edu.ilia.jobsystem.service.dao.sql;

import edu.ilia.jobssystem.models.Job;
import edu.ilia.jobssystem.models.JobServiceResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.sqlite.jdbc4.JDBC4ResultSet;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static edu.ilia.jobsystem.service.dao.sql.SqlQueries.SELECT_JOBS_BY_IDS;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * @author ilia.tankelevich
 * @date 23/02/2020
 */
@RunWith(MockitoJUnitRunner.class)
public class JobsJdbcDaoTest {

    @Mock
    private NamedParameterJdbcTemplate jdbc;

    @InjectMocks
    private JobsJdbcDaoImpl dao;

    @Before
    public void init() {
        SqlRowSet sqlRowSetMock = Mockito.mock(SqlRowSet.class);
        when(sqlRowSetMock.getInt("id")).thenReturn(1);
        when(sqlRowSetMock.getString("name")).thenReturn("test");
        when(sqlRowSetMock.getString("status")).thenReturn("pending");
        when(sqlRowSetMock.getString("content")).thenReturn("test content");
        when(sqlRowSetMock.getString("executionMode")).thenReturn("NOW");
        when(sqlRowSetMock.getString("jobType")).thenReturn("test");
        final AtomicInteger idx = new AtomicInteger(0);
        when(sqlRowSetMock.next()).then((Answer<Boolean>) invocation -> {
            if(idx.intValue() < 5){
                when(sqlRowSetMock.getInt("id")).thenReturn(idx.incrementAndGet());
                when(sqlRowSetMock.getString("name")).thenReturn("test");
                when(sqlRowSetMock.getString("status")).thenReturn("pending");
                when(sqlRowSetMock.getString("content")).thenReturn("test content");
                when(sqlRowSetMock.getString("executionMode")).thenReturn("NOW");
                when(sqlRowSetMock.getString("jobType")).thenReturn("test");
                return true;
            }

            return false;
        });
        when(jdbc.queryForRowSet(eq(SELECT_JOBS_BY_IDS), any(SqlParameterSource.class))).thenReturn(sqlRowSetMock);
    }

    @Test
    public void getJobsTest(){
        List<Integer> jobIds = new ArrayList<>();
        jobIds.add(1);
        jobIds.add(2);
        jobIds.add(3);
        jobIds.add(4);
        jobIds.add(5);
        List<JobServiceResponse> jobs = dao.getJobs(jobIds);
        jobs.forEach(job ->{
            Assert.assertTrue(job instanceof Job);
            assertEquals(((Job)job).getJobType(), "test");
        });
    }
}
