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

public class ReadingRepositoryImpl extends AbstractRepository implements ReadingRepository<String, Reading> {

    public ReadingRepositoryImpl(DBConnectionProvider connectionProvider) {
        super(connectionProvider);
    }
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