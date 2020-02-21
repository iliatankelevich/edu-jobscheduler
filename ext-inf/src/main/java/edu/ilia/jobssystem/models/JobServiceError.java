package edu.ilia.jobssystem.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ilia.tankelevich
 * @date 21/02/2020
 */
@AllArgsConstructor
@Getter
public class JobServiceError extends JobServiceResponse {
    private String message;
}
