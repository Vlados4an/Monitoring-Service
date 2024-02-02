package ru.erma.repository.impl;

import ru.erma.config.DBConnectionProvider;
import ru.erma.exception.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class AbstractRepository {
    protected final DBConnectionProvider connectionProvider;

    public AbstractRepository(DBConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    protected void executeUpdate(String sql,Object... parameters){
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            setParameters(statement, parameters);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to execute update", e);
        }
    }
    private void setParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            statement.setObject(i + 1, parameters[i]);
        }
    }
}
