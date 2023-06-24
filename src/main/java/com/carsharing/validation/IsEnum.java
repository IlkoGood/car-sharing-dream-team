package com.carsharing.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.NotNull;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = EnumValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@NotNull(message = "cannot be null")
@ReportAsSingleViolation
public @interface IsEnum {

    Class<? extends Enum<?>> enumClazz();

    String message() default "is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
