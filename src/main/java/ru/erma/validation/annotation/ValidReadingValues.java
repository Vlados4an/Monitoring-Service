package ru.erma.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.erma.validation.validator.ReadingValuesValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ReadingValuesValidator.class)
public @interface ValidReadingValues {
    String message() default "Invalid reading values";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}