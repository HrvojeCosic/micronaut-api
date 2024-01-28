package com.base.validations;

import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.validation.validator.constraints.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

@Introspected
public class CustomStatusValidator implements ConstraintValidator<ValidPetStatus, String> {

    private final List<String> validStatuses = List.of("available", "pending", "adopted");

    @Override
    public void initialize(ValidPetStatus constraintAnnotation) {
    }

    @Override
    public boolean isValid(@Nullable String value, @NonNull AnnotationValue<ValidPetStatus> annotationMetadata, io.micronaut.validation.validator.constraints.@NonNull ConstraintValidatorContext context) {
        return value != null && validStatuses.contains(value);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && validStatuses.contains(value);
    }
}
