package ru.erma.config;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

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
     */
    public void migration() {
        try(Connection connection = connectionProvider.getConnection()) {
            createSchemaForMigration(connection);
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setLiquibaseSchemaName(schemaName);
            Liquibase liquibase = new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), database);
            liquibase.update();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
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