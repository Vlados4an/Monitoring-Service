package ru.erma.dto;

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
public record ReadingRequest(String username,Integer month,Integer year,Map<String,Integer> values) {

}
