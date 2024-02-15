package ru.erma.config;


import java.sql.Connection;
import java.sql.DriverManager;

/**
 * The DBConnectionProvider class is responsible for managing the database connection.
 * It uses the JDBC DriverManager to establish a connection to the database.
 * The database URL, username, and password are provided as constructor parameters.
 */
public class DBConnectionProvider {
    private final String url;

    private final String username;

    private final String password;

    /**
     * Constructs a new DBConnectionProvider instance.
     * The constructor takes the database URL, username, password, and driver class name as parameters.
     * It attempts to load the driver class, and if unsuccessful, it throws a RuntimeException.
     *
     * @param url the database URL
     * @param username the database username
     * @param password the database password
     * @param driver the fully qualified name of the JDBC driver class
     * @throws RuntimeException if the driver class cannot be loaded
     */
    public DBConnectionProvider(String url, String username, String password, String driver) {
        this.url = url;
        this.username = username;
        this.password = password;
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Opens and returns a connection to the database.
     * It uses the DriverManager's getConnection method to establish the connection.
     * If a connection cannot be established, it throws a RuntimeException.
     *
     * @return a Connection object representing a connection to the database
     * @throws RuntimeException if a database access error occurs
     */
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
