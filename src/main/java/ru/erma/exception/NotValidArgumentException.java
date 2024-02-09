package ru.erma.exception;

/**
 * The NotValidArgumentException class is a custom exception class in Java.
 * It extends the RuntimeException class, which means it is an unchecked exception.
 * It is used to indicate that an invalid argument has been passed to a method.
 * It contains one constructor that takes a message as a parameter.
 * The message represents the detail message of the exception, which is saved for later retrieval by the Throwable.getMessage() method.
 */
public class NotValidArgumentException extends RuntimeException {

    /**
     * Constructs a new NotValidArgumentException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     */
    public NotValidArgumentException(String message) {
        super(message);
    }
}
