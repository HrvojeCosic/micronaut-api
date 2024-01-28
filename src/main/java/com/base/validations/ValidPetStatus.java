package com.base.validations;

import io.micronaut.core.annotation.Introspected;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CustomStatusValidator.class)
public @interface ValidPetStatus {

    String message() default "Invalid pet status";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
