package ru.erma.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.erma.validation.annotation.ValidReadingValues;

import java.util.Map;

@Schema(description = "Data object representing a request for reading data")
public record ReadingRequest(
        @Schema(description = "Username of the user making the request", example = "john_doe")
        @NotBlank(message = "Username must be not blank.")
        String username,

        @Schema(description = "Month for which the reading data is requested", example = "1")
        @NotNull(message = "Month should not be null")
        @Min(message = "Month should be not less than 1", value = 1)
        @Max(message = "Month should be not greater than 12", value = 12)
        Integer month,

        @Schema(description = "Year for which the reading data is requested", example = "2022")
        @NotNull(message = "Year should not be null")
        @Min(message = "Year should be not less than 2000", value = 2000)
        Integer year,

        @Schema(description = "Map of reading types and their corresponding values", example = "{\"cold_water\": 56, \"heating\": 40, \"hot_water\": 44}")
        @ValidReadingValues
        Map<String,Integer> values) {
}