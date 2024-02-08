package ru.erma.exception;

/**
 * The type Player not found exception.
 */
public class UserNotFoundException extends RuntimeException {
    /**
     * Instantiates a new Player not found exception.
     *
     * @param message the message
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
