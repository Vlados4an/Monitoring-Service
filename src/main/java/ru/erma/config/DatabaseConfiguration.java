package ru.erma.config;

import ru.erma.util.PropertyLoader;

import java.util.Properties;

/**
 * The DatabaseConfiguration class is responsible for loading and managing the database configuration properties.
 * It reads the properties from a file and provides methods to configure the database connection and perform database migrations.
 */
public class DatabaseConfiguration {
    private final Properties properties;

    /**
     * Constructs a new DatabaseConfiguration instance.
     * The constructor takes a PropertyLoader as a parameter, which is used to load the properties from a file.
     *
     * @param propertyLoader the PropertyLoader used to load the properties
     */
    public DatabaseConfiguration(PropertyLoader propertyLoader) {
        properties = propertyLoader.getProperties();
    }

    /**
     * Configures and returns a DBConnectionProvider instance using the properties loaded from the file.
     * The DBConnectionProvider is configured with the database URL, username, and password from the properties file.
     *
     * @return a DBConnectionProvider instance configured with the database URL, username, and password from the properties file
     */
    public DBConnectionProvider getConnectionProvider() {
        String dbUrl = properties.getProperty("db.url");
        String dbUser = properties.getProperty("db.user");
        String dbPassword = properties.getProperty("db.password");
        String dbDriver = properties.getProperty("db.driver");
        return new DBConnectionProvider(dbUrl, dbUser, dbPassword, dbDriver);
    }

    /**
     * Performs a database migration using the properties loaded from the file.
     * It creates a DBMigrationService instance and calls its migration method.
     * The DBMigrationService is configured with the schema name and change log file path from the properties file.
     *
     * @return a DBMigrationService instance configured with the schema name and change log file path from the properties file
     */
    public DBMigrationService getMigrationService() {
        String changeLogFile = properties.getProperty("liquibase.changeLogFile");
        String schemaName = properties.getProperty("liquibase.schemaName");
        DBConnectionProvider connectionProvider = getConnectionProvider();

        return new DBMigrationService(connectionProvider, schemaName, changeLogFile);
    }
}