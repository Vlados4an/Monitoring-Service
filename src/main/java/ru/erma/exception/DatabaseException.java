package ru.erma.exception;

/**
 * This class represents a custom exception that is thrown when there is an issue interacting with the database.
 * It extends the RuntimeException class, meaning it's an unchecked exception.
 */
public class DatabaseException extends RuntimeException{

    /**
     * Constructs a new DatabaseException with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the Throwable.getMessage() method).
     * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method).
     *              (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}