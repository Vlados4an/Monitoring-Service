package ru.erma.repository.impl;

import ru.erma.config.DBConnectionProvider;
import ru.erma.model.Reading;
import ru.erma.repository.ReadingRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadingRepositoryImpl implements ReadingRepository<String, Reading> {
    private final DBConnectionProvider connectionProvider;

    public ReadingRepositoryImpl(DBConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public void save(String username, Reading reading) {
        StringBuilder sql = new StringBuilder("INSERT INTO readings (username, month, year");
        StringBuilder values = new StringBuilder(" VALUES (?, ?, ?");
        Map<String, Integer> readingValues = reading.getValues();
        for (String key : readingValues.keySet()) {
            sql.append(", ").append(key);
            values.append(", ?");
        }
        sql.append(")").append(values).append(")");
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            statement.setString(1, username);
            statement.setInt(2, reading.getMonth());
            statement.setInt(3, reading.getYear());
            int index = 4;
            for (Integer value : readingValues.values()) {
                statement.setDouble(index++, value);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Reading> findByUsername(String username) {
        List<Reading> readings = new ArrayList<>();
        String sql = "SELECT * FROM readings WHERE username = ?";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Reading reading = getReadingFromResultSet(resultSet);
                readings.add(reading);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return readings;
    }

    @Override
    public List<Reading> findByUsernameAndMonthAndYear(String username, int month, int year) {
        List<Reading> readings = new ArrayList<>();
        String sql = "SELECT * FROM readings WHERE username = ? AND month = ? AND year = ?";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setInt(2, month);
            statement.setInt(3, year);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Reading reading = getReadingFromResultSet(resultSet);
                readings.add(reading);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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