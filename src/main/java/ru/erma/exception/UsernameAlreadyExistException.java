package ru.erma.exception;

/**
 * This class represents a custom exception that is thrown when a username already exists in the system.
 * It extends the RuntimeException class, meaning it's an unchecked exception.
 */
public class UsernameAlreadyExistException extends RuntimeException {
    /**
     * Constructor for the UsernameAlreadyExistException class.
     *
     * @param message The detail message string of the exception. The detail message is saved for
     *                later retrieval by the Throwable.getMessage() method.
     */
    public UsernameAlreadyExistException(String message){
        super(message);
    }
}
