package ru.erma.repository.impl;

import ru.erma.config.DBConnectionProvider;
import ru.erma.exception.DatabaseException;
import ru.erma.model.Reading;
import ru.erma.repository.ReadingRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * The ReadingRepositoryImpl class provides an implementation of the ReadingRepository interface.
 * It provides methods to save and retrieve reading records from the database.
 */
public class ReadingRepositoryImpl extends AbstractRepository implements ReadingRepository<String, Reading> {
    /**
     * Constructs a new ReadingRepositoryImpl with the specified connection provider.
     *
     * @param connectionProvider the provider for database connections.
     */
    public ReadingRepositoryImpl(DBConnectionProvider connectionProvider) {
        super(connectionProvider);
    }
    /**
     * Saves a reading record to the database.
     * It inserts a new row into the readings table with the username and reading details.
     * If the reading record is null, it throws a DatabaseException.
     *
     * @param username the username associated with the reading.
     * @param reading the reading record to save.
     * @throws DatabaseException if the reading record is null.
     */
    @Override
    public void save(String username, Reading reading) {
        if (reading == null) {
            throw new DatabaseException("Reading cannot be null",new NullPointerException());
        }
        StringBuilder sql = new StringBuilder("INSERT INTO develop.readings (username, month, year");
        StringBuilder values = new StringBuilder(" VALUES (?, ?, ?");
        Map<String, Integer> readingValues = reading.getValues();
        for (String key : readingValues.keySet()) {
            sql.append(", ").append(key);
            values.append(", ?");
        }
        sql.append(")").append(values).append(")");
        List<Object> parameters = new ArrayList<>();
        parameters.add(username);
        parameters.add(reading.getMonth());
        parameters.add(reading.getYear());
        parameters.addAll(readingValues.values());
        executeUpdate(sql.toString(), parameters.toArray());
    }

    /**
     * Retrieves all reading records for a specific username from the database.
     * It selects all rows from the readings table where the username matches the provided username.
     * If there is an error retrieving the reading records, it throws a DatabaseException.
     *
     * @param username the username to find reading records for.
     * @return a list of all reading records for the specified username from the database.
     * @throws DatabaseException if there is an error retrieving the reading records.
     */
    @Override
    public List<Reading> findByUsername(String username) {
        String sql = "SELECT * FROM develop.readings WHERE username = ?";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                return getReadingsFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find readings by username", e);
        }
    }
    /**
     * Retrieves all reading records for a specific username, month, and year from the database.
     * It selects all rows from the readings table where the username, month, and year match the provided values.
     * If there is an error retrieving the reading records, it throws a DatabaseException.
     *
     * @param username the username to find reading records for.
     * @param month the month to find reading records for.
     * @param year the year to find reading records for.
     * @return a list of all reading records for the specified username, month, and year from the database.
     * @throws DatabaseException if there is an error retrieving the reading records.
     */
    @Override
    public List<Reading> findByUsernameAndMonthAndYear(String username, int month, int year) {
        String sql = "SELECT * FROM develop.readings WHERE username = ? AND month = ? AND year = ?";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setInt(2, month);
            statement.setInt(3, year);
            try (ResultSet resultSet = statement.executeQuery()) {
                return getReadingsFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find readings by username, month and year", e);
        }
    }
    /**
     * Creates a list of Reading instances from a result set.
     * It iterates over the result set and creates a Reading instance for each row.
     * If there is an error creating the Reading instances, it throws a DatabaseException.
     *
     * @param resultSet the result set.
     * @return a list of Reading instances.
     * @throws DatabaseException if there is an error creating the Reading instances.
     */
    private List<Reading> getReadingsFromResultSet(ResultSet resultSet) {
        List<Reading> readings = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Reading reading = getReadingFromResultSet(resultSet);
                readings.add(reading);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get readings from result set", e);
        }
        return readings;
    }
    /**
     * Creates a Reading instance from a row in the result set.
     * It gets the month, year, and values from the result set and sets them in the Reading instance.
     *
     * @param resultSet the result set.
     * @return a Reading instance with the month, year, and values from the result set.
     * @throws SQLException if there is an error getting the month, year, or values from the result set.
     */
    private Reading getReadingFromResultSet(ResultSet resultSet) throws SQLException {
        Reading reading = new Reading();
        reading.setMonth(resultSet.getInt("month"));
        reading.setYear(resultSet.getInt("year"));
        ResultSetMetaData metaData = resultSet.getMetaData();
        Map<String, Integer> values = new HashMap<>();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            String columnName = metaData.getColumnName(i);
            if (!columnName.equals("id") && !columnName.equals("username") && !columnName.equals("month") && !columnName.equals("year")) {
                values.put(columnName, resultSet.getInt(columnName));
            }
        }
        reading.setValues(values);
        return reading;
    }
}