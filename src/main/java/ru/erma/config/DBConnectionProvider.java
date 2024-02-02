package ru.erma.config;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * The DBConnectionProvider class is a record that encapsulates the details required to establish a connection to a database.
 * It contains the URL, username, and password required to connect to the database.
 * This class provides a method to get a connection to the database using the DriverManager's getConnection method.
 *
 * @param url The URL of the database to connect to.
 * @param username The username to use when connecting to the database.
 * @param password The password to use when connecting to the database.
 */
public record DBConnectionProvider(String url, String username, String password) {

    /**
     * This method attempts to establish a connection to the database using the DriverManager's getConnection method.
     * The URL, username, and password provided when creating an instance of this class are used to establish the connection.
     * If a connection cannot be established for any reason, a RuntimeException is thrown.
     *
     * @return A Connection object representing a connection to the database.
     * @throws RuntimeException If a connection cannot be established.
     */
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}