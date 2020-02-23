package edu.ilia.jobssystem.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author ilia.tankelevich
 * @date 21/02/2020
 */
@Data
public class Job extends JobServiceResponse {
    private String name;
    @JsonProperty("job_type")
    private String jobType;
    private String content;
    private String status;
    @JsonProperty("execution_mode")
    private ExecutionMode executionMode;
    private String time;
}
