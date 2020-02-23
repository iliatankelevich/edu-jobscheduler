package edu.ilia.jobssystem.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ilia.tankelevich
 * @date 21/02/2020
 */

@Getter
public class JobServiceError extends JobServiceResponse {
    public JobServiceError(int id, String message) {
        this.message = message;
        this.setId(id);
    }

    private String message;
}
