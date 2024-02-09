package ru.erma.exception;

/**
 * The RegisterException class is a custom exception class in Java.
 * It extends the RuntimeException class, which means it is an unchecked exception.
 * It is used to indicate that a registration error has occurred in the system.
 * It contains one constructor that takes a message as a parameter.
 * The message represents the detail message of the exception, which is saved for later retrieval by the Throwable.getMessage() method.
 */
public class RegisterException extends RuntimeException {

    /**
     * Constructs a new RegisterException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     */
    public RegisterException(String message) {
        super(message);
    }
}
