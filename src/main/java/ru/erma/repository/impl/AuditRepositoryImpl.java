package ru.erma.repository.impl;

import ru.erma.config.DBConnectionProvider;
import ru.erma.exception.DatabaseException;
import ru.erma.model.Audit;
import ru.erma.repository.AuditRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuditRepositoryImpl extends AbstractRepository implements AuditRepository<Audit> {

    public AuditRepositoryImpl(DBConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    @Override
    public void save(Audit audit) {
        if (audit == null) {
            throw new DatabaseException("Audit cannot be null",new NullPointerException());
        }
        String sql = "INSERT INTO develop.audits (action) VALUES (?)";
        for (String action : audit.getAudits()) {
            executeUpdate(sql, action);
        }
    }

    @Override
    public List<Audit> findAll() {
        List<Audit> audits = new ArrayList<>();
        String sql = "SELECT * FROM develop.audits";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Audit audit = getAuditFromResultSet(resultSet);
                audits.add(audit);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get audits from result set", e);
        }
        return audits;
    }

    private Audit getAuditFromResultSet(ResultSet resultSet) throws SQLException {
        Audit audit = new Audit();
        audit.setId(resultSet.getLong("id"));
        audit.getAudits().add(resultSet.getString("action"));
        return audit;
    }
}