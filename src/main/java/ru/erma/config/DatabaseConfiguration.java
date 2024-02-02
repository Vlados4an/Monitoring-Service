package ru.erma.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * The DatabaseConfiguration class is responsible for loading and managing the database configuration properties.
 * It reads the properties from a file and provides methods to configure the database connection and perform database migrations.
 */
public class DatabaseConfiguration {
    private static Properties properties;
    private static final String PROPERTIES_FILEPATH = getPropertiesFilepath();

    /**
     * Configures and returns a DBConnectionProvider instance using the properties loaded from the file.
     *
     * @return a DBConnectionProvider instance configured with the database URL, username, and password from the properties file.
     */
    public static DBConnectionProvider connectionProviderConfiguration() {
        String dbUrl = properties.getProperty("db.url");
        String dbUser = properties.getProperty("db.user");
        String dbPassword = properties.getProperty("db.password");
        return new DBConnectionProvider(dbUrl, dbUser, dbPassword);
    }

    /**
     * Performs a database migration using the properties loaded from the file.
     * It creates a DBMigrationService instance and calls its migration method.
     */
    public static void databaseMigration(){
        String changeLogFile = properties.getProperty("liquibase.changeLogFile");
        String schemaName = properties.getProperty("liquibase.schemaName");
        DBConnectionProvider connectionProvider = connectionProviderConfiguration();

        DBMigrationService migrationService = new DBMigrationService(connectionProvider, schemaName, changeLogFile);
        migrationService.migration();
    }

    /**
     * Loads the properties from the file if they have not been loaded yet.
     * If there is an error reading the file, it throws a RuntimeException.
     */
    public static void loadProperties() {
        if (properties == null) {
            properties = new Properties();
            try (InputStream stream = Files.newInputStream(Paths.get(PROPERTIES_FILEPATH))) {
                properties.load(stream);
            } catch (IOException e) {
                throw new RuntimeException("Error reading configuration file: " + e.getMessage());
            }
        }
    }

    /**
     * Returns the path to the properties file.
     *
     * @return the path to the properties file.
     */
    private static String getPropertiesFilepath() {
        return "src" + File.separator + "main" + File.separator + "resources" + File.separator + "application.properties";
    }
}