package ru.erma.exception;

/**
 * The AuthorizeException class is a custom exception class in Java.
 * It extends the RuntimeException class, which means it is an unchecked exception.
 * It is used to indicate that an authorization error has occurred in the system.
 * It contains one constructor that takes a message as a parameter.
 * The message represents the detail message of the exception, which is saved for later retrieval by the Throwable.getMessage() method.
 */
public class AuthorizeException extends RuntimeException {
    /**
     * Constructs a new AuthorizeException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     */
    public AuthorizeException(String message) {
        super(message);
    }
}
