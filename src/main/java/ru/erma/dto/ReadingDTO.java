package ru.erma.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Data object representing a user's reading")
public class ReadingDTO {
    @Schema(description = "Month of the reading", example = "1")
    private Integer month;

    @Schema(description = "Year of the reading", example = "2022")
    private Integer year;

    @Schema(description = "Map of reading values", example = "{\"cold_water\": 56, \"heating\": 40, \"hot_water\": 44}")
    private Map<String,Integer> values;
}