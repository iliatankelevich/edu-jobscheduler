package edu.ilia.jobssystem.models;

import lombok.Data;
import java.util.Date;

/**
 * @author ilia.tankelevich
 * @date 21/02/2020
 */
@Data
public class Job extends JobServiceResponse {
    private String name;
    private String jobType;
    private String content;
    private String status;
    private ExecutionTime executionTime;
    private Date time;
}
