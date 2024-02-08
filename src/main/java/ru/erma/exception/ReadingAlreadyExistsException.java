package ru.erma.exception;

/**
 * The ReadingAlreadyExistsException class is a custom exception class in Java.
 * It is used to indicate that a reading already exists in the system.
 * It contains one constructor that takes a message as a parameter.
 * The message represents the detail message of the exception, which is saved for later retrieval by the Throwable.getMessage() method.
 */
public class ReadingAlreadyExistsException extends RuntimeException{
    /**
     * Constructs a new ReadingAlreadyExistsException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     */
    public ReadingAlreadyExistsException(String message) {
        super(message);
    }
}
