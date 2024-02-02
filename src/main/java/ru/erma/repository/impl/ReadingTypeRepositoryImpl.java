package ru.erma.repository.impl;

import ru.erma.config.DBConnectionProvider;
import ru.erma.exception.DatabaseException;
import ru.erma.repository.ReadingTypeRepository;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReadingTypeRepositoryImpl extends AbstractRepository implements ReadingTypeRepository<String> {

    public ReadingTypeRepositoryImpl(DBConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    public void addColumnToReadingsTable(String columnName) {
        String sql = "ALTER TABLE develop.readings ADD COLUMN " + columnName + " double precision";
        executeUpdate(sql);
    }

    public void removeColumnFromReadingsTable(String columnName) {
        String sql = "ALTER TABLE develop.readings DROP COLUMN " + columnName;
        executeUpdate(sql);
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
            throw new DatabaseException("Failed to get reading types from database", e);
        }
        return readingTypes;
    }
}