package ru.erma.model;

import java.util.List;

/**
 * The User class represents users in the system.
 * Each user has a username, password and a list of readings.
 */
public class User {
    private String username;

    private byte[] password;

    private List<Reading> readings;


    public User() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public List<Reading> getReadings() {
        return readings;
    }

    public void setReadings(List<Reading> readings) {
        this.readings = readings;
    }
}
