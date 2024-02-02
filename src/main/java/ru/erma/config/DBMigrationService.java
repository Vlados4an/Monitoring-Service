package ru.erma.config;

import liquibase.command.CommandScope;
import liquibase.command.core.UpdateCommandStep;
import liquibase.exception.CommandExecutionException;
import ru.erma.exception.DatabaseException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The DBMigrationService class is responsible for managing database migrations.
 * It uses Liquibase to apply changes to the database schema.
 */
public class DBMigrationService {

    private final DBConnectionProvider connectionProvider;
    private final String schemaName;
    private final String changeLogFile;

    /**
     * Constructs a new DBMigrationService with the specified connection provider, schema name, and change log file.
     *
     * @param connectionProvider the provider for database connections.
     * @param schemaName the name of the database schema.
     * @param changeLogFile the path to the Liquibase change log file.
     */
    public DBMigrationService(DBConnectionProvider connectionProvider, String schemaName, String changeLogFile) {
        this.connectionProvider = connectionProvider;
        this.schemaName = processedSchemaName(schemaName);
        this.changeLogFile = changeLogFile;
    }

    /**
     * Performs the database migration.
     * It creates a new schema if it doesn't exist, then applies the Liquibase changes.
     * It uses the CommandScope class from Liquibase to execute the update command.
     * If the update command fails, it prints an error message.
     * If the connection to the database fails, it throws a DatabaseException.
     */
    public void migration() {
        try(Connection connection = connectionProvider.getConnection()) {
            createSchemaForMigration(connection);
            try {
                new CommandScope(UpdateCommandStep.COMMAND_NAME)
                        .addArgumentValue("defaultSchemaName",schemaName)
                        .addArgumentValue(UpdateCommandStep.CHANGELOG_FILE_ARG,changeLogFile)
                        .addArgumentValue("url", connectionProvider.url())
                        .addArgumentValue("username", connectionProvider.username())
                        .addArgumentValue("password", connectionProvider.password())
                        .execute();
            } catch (CommandExecutionException e) {
                System.out.println("Error running update: "+e.getMessage());
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to execute update",e);
        }
    }

    /**
     * Creates a new schema for the migration if it doesn't exist.
     *
     * @param connection the database connection.
     * @throws SQLException if there is an error executing the SQL statement.
     */
    private void createSchemaForMigration(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "CREATE SCHEMA IF NOT EXISTS " + schemaName;
        statement.executeUpdate(sql);
        statement.close();
    }

    /**
     * Processes the schema name by removing any non-alphabetic characters.
     *
     * @param schemaName the original schema name.
     * @return the processed schema name.
     */
    private String processedSchemaName(String schemaName) {
        return schemaName.split("[^\\p{L}]")[0];
    }
}