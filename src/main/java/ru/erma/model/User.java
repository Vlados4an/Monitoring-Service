package ru.erma.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Default constructor for the AuditLog class.
 * Initializes the list of log entries.
 */
@Getter
@Setter
@NoArgsConstructor
public class User {
    private String username;
    private String password;
    private String salt;
    private List<Reading> readings;

    /**
     * Constructor for the User class.
     * Initializes the username, password, and salt.
     *
     * @param username the username of the user
     * @param password the hashed password of the user
     * @param salt the salt used for password hashing
     */
    public User(String username, String password, String salt) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.readings = new ArrayList<>();
    }
}
