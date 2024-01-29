package ru.erma.model;

import lombok.*;

import java.util.Map;

/**
 * This class represents a Reading in the system.
 * A Reading has a month, year, and a map of values.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Reading {
    private Integer month;
    private Integer year;
    private Map<String,Integer> values;

    /**
     * Returns a string representation of the Reading.
     * The string includes the month, year, and values of the reading.
     *
     * @return a string representation of the Reading
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Month: ").append(month).append("\n");
        sb.append("Year: ").append(year).append("\n");
        sb.append("Values: \n");
        for (Map.Entry<String, Integer> entry : values.entrySet()) {
            sb.append(" - ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}
