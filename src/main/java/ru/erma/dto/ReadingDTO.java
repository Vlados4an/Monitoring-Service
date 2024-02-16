package ru.erma.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * This class represents a Reading in the system.
 * A Reading has a month, year, and a map of values.
 */
@Getter
@Setter
@NoArgsConstructor
public class ReadingDTO {
    private Integer month;

    private Integer year;

    private Map<String,Integer> values;
}
