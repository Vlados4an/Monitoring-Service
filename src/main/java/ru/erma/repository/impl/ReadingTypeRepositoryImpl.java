package ru.erma.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.erma.repository.ReadingTypeRepository;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class ReadingTypeRepositoryImpl implements ReadingTypeRepository<String> {

    private final JdbcTemplate jdbcTemplate;

    public ReadingTypeRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addColumnToReadingsTable(String columnName) {
        String sql = "ALTER TABLE develop.readings ADD COLUMN " + columnName + " double precision";
        jdbcTemplate.execute(sql);
    }

    public void removeColumnFromReadingsTable(String columnName) {
        String sql = "ALTER TABLE develop.readings DROP COLUMN " + columnName;
        jdbcTemplate.execute(sql);
    }

    public List<String> getReadingTypesFromDatabase() {
        String sql = "SELECT column_name FROM information_schema.columns WHERE table_name = 'readings' AND table_schema = 'develop' AND column_name NOT IN ('id', 'username', 'month', 'year')";
        return jdbcTemplate.query(sql, (ResultSet resultSet, int i) -> resultSet.getString("column_name"));
    }
}