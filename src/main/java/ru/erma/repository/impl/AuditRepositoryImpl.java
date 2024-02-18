package ru.erma.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.erma.model.Audit;
import ru.erma.repository.AuditRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * This class implements the AuditRepository interface.
 * It provides methods to save and retrieve audit records from the database.
 * It uses the JdbcTemplate to execute SQL queries.
 */
@Repository
public class AuditRepositoryImpl implements AuditRepository<Audit> {

    private final JdbcTemplate jdbcTemplate;

    public AuditRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Audit audit) {
        String sql = "INSERT INTO develop.audits (username, timestamp, action) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, audit.getUsername(), audit.getTimestamp(), audit.getAction());
    }

    @Override
    public List<Audit> findAll() {
        String sql = "SELECT username, timestamp, action FROM develop.audits";
        return jdbcTemplate.query(sql, new AuditRowMapper());
    }

    private static class AuditRowMapper implements RowMapper<Audit> {
        @Override
        public Audit mapRow(ResultSet resultSet, int i) throws SQLException {
            Audit audit = new Audit();
            audit.setUsername(resultSet.getString("username"));
            audit.setTimestamp(resultSet.getTimestamp("timestamp").toLocalDateTime());
            audit.setAction(resultSet.getString("action"));
            return audit;
        }
    }
}