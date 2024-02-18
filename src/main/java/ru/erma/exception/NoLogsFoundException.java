package ru.erma.exception;

/**
 * This exception is thrown when no logs are found.
 */
public class NoLogsFoundException extends RuntimeException {

    /**
     * Constructs a new NoLogsFoundException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     */
    public NoLogsFoundException(String message) {
        super(message);
    }
}