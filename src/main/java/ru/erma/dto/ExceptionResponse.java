package ru.erma.dto;

/**
 * The ExceptionResponse class is a record class in Java.
 * It is used to represent a response that contains an exception message.
 * It contains one field: message.
 * The message field represents the exception message to be sent in the response.
 */
public record ExceptionResponse(String message) {
}
