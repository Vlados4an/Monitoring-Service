package ru.erma.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import ru.erma.service.ReadingStructureService;
import ru.erma.validation.annotation.ValidReadingValues;

import java.util.List;
import java.util.Map;

/**
 * This class validates that the values in a reading are valid.
 * It implements the ConstraintValidator interface provided by Jakarta Validation.
 * The ValidReadingValues annotation is used to apply this validator to a Map<String, Integer>.
 */
@RequiredArgsConstructor
public class ReadingValuesValidator implements ConstraintValidator<ValidReadingValues, Map<String, Integer>> {

    private final ReadingStructureService readingStructureService;

    @Override
    public boolean isValid(Map<String, Integer> values, ConstraintValidatorContext context) {
        for (Integer value : values.values()) {
            if (value == null || value < 0) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Readings must be positive numbers.")
                        .addConstraintViolation();
                return false;
            }
        }
        List<String> validTypes = readingStructureService.getReadingTypes();
        for (String key : values.keySet()) {
            if (!validTypes.contains(key)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Invalid reading type: " + key)
                        .addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}