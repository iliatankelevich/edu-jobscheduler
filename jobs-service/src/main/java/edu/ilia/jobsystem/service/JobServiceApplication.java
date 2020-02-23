package edu.ilia.jobsystem.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.EnableJms;

/**
 * @author ilia.tankelevich
 * @date 21/02/2020
 */
@EnableJms
@SpringBootApplication
public class JobServiceApplication {
    protected static ConfigurableApplicationContext context;
    public static void main(String[] args) {
        context = SpringApplication.run(JobServiceApplication.class, args);
    }
}
