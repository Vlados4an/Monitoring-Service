package ru.erma.model;

import java.util.List;

/**
 * Default constructor for the AuditLog class.
 * Initializes the list of log entries.
 */
public class User {
    private String username;
    private String password;
    private String salt;
    private List<Reading> readings;

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public List<Reading> getReadings() {
        return readings;
    }

    public void setReadings(List<Reading> readings) {
        this.readings = readings;
    }
}
