package ru.erma.repository.impl;

import ru.erma.config.DBConnectionProvider;
import ru.erma.repository.ReadingTypeRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReadingTypeRepositoryImpl implements ReadingTypeRepository<String> {

    private final DBConnectionProvider connectionProvider;

    public ReadingTypeRepositoryImpl(DBConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public void addColumnToReadingsTable(String columnName) {
        String sql = "ALTER TABLE readings ADD COLUMN " + columnName + " double precision";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeColumnFromReadingsTable(String columnName) {
        String sql = "ALTER TABLE readings DROP COLUMN " + columnName;
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<String> getReadingTypesFromDatabase() {
        List<String> readingTypes = new ArrayList<>();
        try (Connection connection = connectionProvider.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getColumns(null, null, "readings", null);
            while (resultSet.next()) {
                String columnName = resultSet.getString("COLUMN_NAME");
                if (!columnName.equals("id") && !columnName.equals("username") && !columnName.equals("month") && !columnName.equals("year")) {
                    readingTypes.add(columnName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return readingTypes;
    }

}