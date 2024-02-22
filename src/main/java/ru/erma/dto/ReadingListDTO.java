package ru.erma.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ru.erma.model.Reading;

import java.util.List;

@Getter
@Setter
@Schema(description = "Data object representing a list of readings")
public class ReadingListDTO {
    @Schema(description = "List of readings")
    private List<Reading> readings;
}