package ru.erma.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import ru.erma.validation.annotation.DateNotInFuture;
import ru.erma.validation.annotation.ValidReadingValues;

import java.util.Map;

/**
 * The ReadingRequest class is a record class in Java.
 * It is used to represent a request for reading data.
 * It contains four fields: username, month, year, and values.
 * The username field represents the username of the user making the request.
 * The month field represents the month for which the reading data is requested.
 * The year field represents the year for which the reading data is requested.
 * The values field represents a map of reading types and their corresponding values.
 */
@DateNotInFuture
public record ReadingRequest(
        @NotBlank(message = "Username must be not blank.")
        String username,
        @NotNull(message = "Month should not be null")
        @Min(message = "Month should be not less than 1", value = 1)
        @Max(message = "Month should be not greater than 12", value = 12)
        Integer month,

        @NotNull(message = "Year should not be null")
        @Min(message = "Year should be not less than 2000", value = 2000)
        Integer year,

        @ValidReadingValues
        Map<String,Integer> values) {

}
