package ru.erma.validation.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import ru.erma.validation.validator.DateNotInFutureValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateNotInFutureValidator.class)
public @interface DateNotInFuture {
    String message() default "Date should not be in the future";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}