package ru.erma.config;


import java.sql.Connection;
import java.sql.DriverManager;

/**
 * The database connection provider class.
 */
public class DBConnectionProvider {
    private final String url;
    private final String username;
    private final String password;

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
     * Open connection to database.
     *
     * @return the connection
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
