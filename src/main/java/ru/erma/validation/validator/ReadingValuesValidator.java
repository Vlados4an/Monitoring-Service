package ru.erma.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.erma.service.ReadingStructureService;
import ru.erma.validation.annotation.ValidReadingValues;

import java.util.List;
import java.util.Map;

public class ReadingValuesValidator implements ConstraintValidator<ValidReadingValues, Map<String, Integer>> {

    private final ReadingStructureService readingStructureService;

    public ReadingValuesValidator(ReadingStructureService readingStructureService) {
        this.readingStructureService = readingStructureService;
    }

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