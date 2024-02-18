package ru.erma.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.erma.dto.ReadingRequest;
import ru.erma.validation.annotation.DateNotInFuture;

import java.time.DateTimeException;
import java.time.YearMonth;

/**
 * This class validates that the date in a ReadingRequest is not in the future.
 * It implements the ConstraintValidator interface provided by Jakarta Validation.
 * The DateNotInFuture annotation is used to apply this validator to a ReadingRequest.
 */
public class DateNotInFutureValidator implements ConstraintValidator<DateNotInFuture, ReadingRequest> {
    @Override
    public boolean isValid(ReadingRequest value, ConstraintValidatorContext context) {
        if (value.year() == null || value.month() == null) {
            return true;
        }
        try {
            YearMonth yearMonth = YearMonth.of(value.year(), value.month());
            boolean valid = yearMonth.isBefore(YearMonth.now()) || yearMonth.equals(YearMonth.now());
            if (!valid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Date should not be in the future")
                        .addConstraintViolation();
            }
            return valid;
        } catch (DateTimeException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid date")
                    .addConstraintViolation();
            return false;
        }
    }
}