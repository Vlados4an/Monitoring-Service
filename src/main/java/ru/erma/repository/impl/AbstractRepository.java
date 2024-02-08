package ru.erma.repository.impl;

import ru.erma.config.DBConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The AbstractRepository class provides a base for all repository classes.
 * It provides a method to execute SQL update statements.
 */
public abstract class AbstractRepository {
    protected final DBConnectionProvider connectionProvider;

    protected AbstractRepository(DBConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    /**
     * Executes an SQL update statement.
     * It opens a connection to the database, prepares the statement with the provided SQL and parameters, and executes the update.
     * If there is an error executing the update, it throws a DatabaseException.
     *
     * @param sql the SQL statement to execute.
     * @param parameters the parameters to set in the SQL statement.
     * @throws RuntimeException if there is an error executing the update.
     */
    protected void executeUpdate(String sql, Object... parameters){
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            setParameters(statement, parameters);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute update: " + e.getMessage());
        }
    }

    /**
     * Sets the parameters in the prepared statement.
     * It iterates over the parameters and sets each one in the statement.
     *
     * @param statement the prepared statement.
     * @param parameters the parameters to set in the statement.
     * @throws SQLException if there is an error setting the parameters.
     */
    private void setParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            statement.setObject(i + 1, parameters[i]);
        }
    }
}