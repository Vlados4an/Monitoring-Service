package ru.erma.model;

import java.util.Map;

/**
 * This class represents a Reading in the system.
 * A Reading has a month, year, and a map of values.
 */
public class Reading {
    private Integer month;
    private Integer year;
    private Map<String,Integer> values;

    public Reading() {
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Map<String, Integer> getValues() {
        return values;
    }

    public void setValues(Map<String, Integer> values) {
        this.values = values;
    }
}
