package edu.ilia.jobsystem.service.dao.sql;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * @author ilia.tankelevich
 * @date 22/02/2020
 */
@Configuration
public class SqlConfiguration {
    @Bean
    public SimpleJdbcInsert getSimpleJdbcInsert(JdbcTemplate jdbc, @Value("${jobs.db.table}") String tableName){
        return new SimpleJdbcInsert(jdbc).withTableName(tableName);
    }
}
