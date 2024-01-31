package ru.erma.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class DatabaseConfiguration {
    private static Properties properties;
    private static final String PROPERTIES_FILEPATH = getPropertiesFilepath();

    public static DBConnectionProvider connectionProviderConfiguration() {
        String dbUrl = properties.getProperty("db.url");
        String dbUser = properties.getProperty("db.user");
        String dbPassword = properties.getProperty("db.password");
        return new DBConnectionProvider(dbUrl, dbUser, dbPassword);
    }
    public static void databaseMigration(){
        String changeLogFile = properties.getProperty("liquibase.changeLogFile");
        String schemaName = properties.getProperty("liquibase.schemaName");
        DBConnectionProvider connectionProvider = connectionProviderConfiguration();

        DBMigrationService migrationService = new DBMigrationService(connectionProvider, schemaName, changeLogFile);
        migrationService.migration();
    }

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
    private static String getPropertiesFilepath() {
        return "src" + File.separator + "main" + File.separator + "resources" + File.separator + "application.properties";
    }

}

