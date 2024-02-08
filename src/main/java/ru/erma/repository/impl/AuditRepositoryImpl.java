package ru.erma.repository.impl;

import ru.erma.config.DBConnectionProvider;
import ru.erma.model.Audit;
import ru.erma.repository.AuditRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The AuditRepositoryImpl class provides an implementation of the AuditRepository interface.
 * It provides methods to save and retrieve audit records from the database.
 */
public class AuditRepositoryImpl extends AbstractRepository implements AuditRepository<Audit> {

    /**
     * Constructs a new AuditRepositoryImpl with the specified connection provider.
     *
     * @param connectionProvider the provider for database connections.
     */
    public AuditRepositoryImpl(DBConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    /**
     * Saves an audit record to the database.
     * It inserts a new row into the audits table for each action in the audit record.
     * If the audit record is null, it throws a DatabaseException.
     *
     * @param audit the audit record to save.
     * @throws RuntimeException if the audit record is null.
     */
    @Override
    public void save(Audit audit) {
        String sql = "INSERT INTO develop.audits (action) VALUES (?)";
        for (String action : audit.getAudits()) {
            executeUpdate(sql, action);
        }
    }

    /**
     * Retrieves all audit records from the database.
     * It selects all rows from the audits table and creates an Audit instance for each one.
     * If there is an error retrieving the audit records, it throws a DatabaseException.
     *
     * @return a list of all audit records from the database.
     * @throws RuntimeException if there is an error retrieving the audit records.
     */
    @Override
    public List<Audit> findAll() {
        List<Audit> audits = new ArrayList<>();
        String sql = "SELECT id, action FROM develop.audits";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Audit audit = getAuditFromResultSet(resultSet);
                audits.add(audit);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get audits from result set: " + e.getMessage());
        }
        return audits;
    }

    /**
     * Creates an Audit instance from a row in the result set.
     * It gets the id and action from the result set and adds them to the Audit instance.
     *
     * @param resultSet the result set.
     * @return an Audit instance with the id and action from the result set.
     * @throws SQLException if there is an error getting the id or action from the result set.
     */
    private Audit getAuditFromResultSet(ResultSet resultSet) throws SQLException {
        Audit audit = new Audit();
        audit.setId(resultSet.getLong("id"));
        audit.getAudits().add(resultSet.getString("action"));
        return audit;
    }
}