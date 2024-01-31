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

public class AuditRepositoryImpl implements AuditRepository<Audit> {
    private final DBConnectionProvider connectionProvider;

    public AuditRepositoryImpl(DBConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public void save(Audit audit) {
        String sql = "INSERT INTO audits (action) VALUES (?)";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (String action : audit.getAudits()) {
                statement.setString(1, action);
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Audit> findAll() {
        List<Audit> audits = new ArrayList<>();
        String sql = "SELECT * FROM audits";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Audit audit = new Audit();
                audit.setId(resultSet.getLong("id"));
                audit.getAudits().add(resultSet.getString("action"));
                audits.add(audit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return audits;
    }
}