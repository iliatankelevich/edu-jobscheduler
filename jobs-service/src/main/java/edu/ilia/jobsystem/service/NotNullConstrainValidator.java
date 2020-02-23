package edu.ilia.jobsystem.service;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotNull;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

/**
 * @author ilia.tankelevich
 * @date 22/02/2020
 */
@Component
@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class NotNullConstrainValidator implements ConstraintValidator<NotNull, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return value != null;
    }
}
