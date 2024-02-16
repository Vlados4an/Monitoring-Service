package ru.erma.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.erma.model.Reading;
import ru.erma.repository.ReadingRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ReadingRepositoryImpl implements ReadingRepository<String, Reading> {

    private final JdbcTemplate jdbcTemplate;

    public ReadingRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(String username, Reading reading) {
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
        jdbcTemplate.update(sql.toString(), parameters.toArray());
    }

    @Override
    public List<Reading> findByUsername(String username) {
        String sql = "SELECT * FROM develop.readings WHERE username = ?";
        return jdbcTemplate.query(sql, new Object[]{username}, new ReadingRowMapper());
    }

    @Override
    public List<Reading> findByUsernameAndMonthAndYear(String username, int month, int year) {
        String sql = "SELECT * FROM develop.readings WHERE username = ? AND month = ? AND year = ?";
        return jdbcTemplate.query(sql, new Object[]{username, month, year}, new ReadingRowMapper());
    }

    @Override
    public Reading findLatestByUsername(String username) {
        String sql = "SELECT * FROM develop.readings WHERE username = ? ORDER BY year DESC, month DESC LIMIT 1";
        return jdbcTemplate.queryForObject(sql, new Object[]{username}, new ReadingRowMapper());
    }

    private static class ReadingRowMapper implements RowMapper<Reading> {
        @Override
        public Reading mapRow(ResultSet resultSet, int i) throws SQLException {
            Reading reading = new Reading();
            reading.setMonth(resultSet.getInt("month"));
            reading.setYear(resultSet.getInt("year"));
            Map<String, Integer> values = new HashMap<>();
            for (int j = 1; j <= resultSet.getMetaData().getColumnCount(); j++) {
                String columnName = resultSet.getMetaData().getColumnName(j);
                if (!columnName.equals("id") && !columnName.equals("username") && !columnName.equals("month") && !columnName.equals("year")) {
                    values.put(columnName, resultSet.getInt(columnName));
                }
            }
            reading.setValues(values);
            return reading;
        }
    }
}