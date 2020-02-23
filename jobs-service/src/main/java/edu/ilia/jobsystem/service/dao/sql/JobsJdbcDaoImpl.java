package edu.ilia.jobsystem.service.dao.sql;

import edu.ilia.jobssystem.models.ExecutionMode;
import edu.ilia.jobssystem.models.Job;
import edu.ilia.jobssystem.models.JobServiceError;
import edu.ilia.jobssystem.models.JobServiceResponse;
import edu.ilia.jobsystem.service.dao.JobsDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.*;

import static edu.ilia.jobsystem.service.dao.sql.SqlQueries.*;

/**
 * @author ilia.tankelevich
 * @date 21/02/2020
 */
@Repository
public class JobsJdbcDaoImpl implements JobsDao {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${jobs.db.table}")
    private String jobsTableName;

    @Resource
    private NamedParameterJdbcTemplate jdbc;

    @PostConstruct
    @Override
    public void initDB() {
        boolean isExist = false;
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("table_name", jobsTableName);
        SqlRowSet sqlRowSet = jdbc.queryForRowSet(IS_JOBS_TABLE_EXISTS, parameters);
        while (sqlRowSet.next()) {
            isExist = true;
        }

        if (!isExist) {
            log.info("creating DB");
            jdbc.update(CREATE_TABLE, new MapSqlParameterSource());
        }
    }

    @Override
    public Job insertJob(@NotNull(message = "job cannot be null") Job job) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(job);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int updateResult = jdbc.update(INSERT_JOB, parameters, keyHolder);
        if(updateResult > 0){
            job.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        }else{
            String errMsg = "failed to insert job of type " + job.getJobType() + " to DB";
            log.error(errMsg);
            throw new RuntimeException(errMsg);
        }

        return job;
    }

    @Override
    public boolean updateJobStatus(int id, String status) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", id).addValue("status", status);
        int update = jdbc.update(UPDATE_STATUS, parameters);
        return update > 0;
    }

    @Override
    public List<Integer> getJobIdsByExecutionMode(ExecutionMode every2Hours) {
        return null;

    }

    @Override
    public JobServiceResponse getJob(Integer jobId) {
        JobServiceResponse response;
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", jobId);
        SqlRowSet row = jdbc.queryForRowSet(SELECT_JOB_BY_ID, parameters);
        Map<Integer, JobServiceResponse> jobs = buildJobsListFromRowSet(row);
        if(jobs.size() == 1){
            response = jobs.get(0);
        }else{
            response = new JobServiceError(jobId, "no job found for id " + jobId);
        }

        return response;
    }

    @Override
    public boolean deleteJob(int jobId) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", jobId);
        return jdbc.update(DELETE_JOB, parameters) > 0;
    }

    @Override
    public List<JobServiceResponse> getJobs(List<Integer> jobIds) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("ids", jobIds);
        SqlRowSet sqlRowSet = jdbc.queryForRowSet(SELECT_JOBS_BY_IDS, parameters);
        Map<Integer, JobServiceResponse> jobs = buildJobsListFromRowSet(sqlRowSet);
        if(jobs.size() < jobIds.size()){
            jobIds.forEach(jobId -> {
                if(!jobs.containsKey(jobId)){
                    jobs.put(jobId, new JobServiceError(jobId, "no job found for id " + jobId));
                }
            });
        }

        return new ArrayList<>(jobs.values());
    }

    private Map<Integer, JobServiceResponse> buildJobsListFromRowSet(SqlRowSet row) {
        Map<Integer, JobServiceResponse> result = new HashMap<>();
        while(row.next()){
            Job job = new Job();
            job.setId(row.getInt("id"));
            job.setName(row.getString("name"));
            job.setStatus(row.getString("status"));
            job.setContent(row.getString("content"));
            job.setExecutionMode(ExecutionMode.valueOf(row.getString("executionMode")));
            job.setJobType(row.getString("jobType"));
            result.put(job.getId(), job);
        }

        return result;
    }
}
