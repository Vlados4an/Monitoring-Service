package ru.erma.exception;

public class NoLogsFoundException extends RuntimeException {
    public NoLogsFoundException(String message) {
        super(message);
    }
}