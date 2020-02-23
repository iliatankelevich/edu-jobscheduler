package edu.ilia.jobsystem.service.dao.sql;

/**
 * @author ilia.tankelevich
 * @date 22/02/2020
 */
public class SqlQueries {
    static final String IS_JOBS_TABLE_EXISTS = "SELECT name FROM sqlite_master WHERE type='table' AND name = :table_name";
    static final String CREATE_TABLE = "CREATE TABLE 'jobs' (" +
            "'id' INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
            "'name' TEXT NOT NULL," +
            "'jobType' TEXT NOT NULL," +
            "'content' TEXT," +
            "'status' TEXT NOT NULL," +
            "'executionMode' NUMERIC NOT NULL," +
            "'time' INTEGER," +
            "'created' TEXT NOT NULL," +
            "'updated' TEXT NOT NULL" +
            ");";
    static final String INSERT_JOB = "INSERT INTO jobs (name, jobType, content, status, executionMode, time, created, updated) " +
            "VALUES (:name, :jobType, :content, :status, :executionMode, :time, datetime('now'), datetime('now'))";
    static final String UPDATE_STATUS = "UPDATE jobs SET status = :status, updated = datetime('now') WHERE id = :id";
    static final String SELECT_JOB_BY_ID = "SELECT * FROM jobs WHERE id = :id";
    static final String SELECT_JOBS_BY_IDS = "SELECT * FROM jobs WHERE id in (:ids)";
    static final String DELETE_JOB = "DELETE FROM jobs WHERE id = :id";
}
