package ru.erma.repository.impl;

import ru.erma.config.DBConnectionProvider;
import ru.erma.repository.ReadingTypeRepository;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The ReadingTypeRepositoryImpl class provides an implementation of the ReadingTypeRepository interface.
 * It provides methods to add and remove columns from the readings table, and to get the reading types from the database.
 */
public class ReadingTypeRepositoryImpl extends AbstractRepository implements ReadingTypeRepository<String> {

    /**
     * Constructs a new ReadingTypeRepositoryImpl with the specified connection provider.
     *
     * @param connectionProvider the provider for database connections.
     */
    public ReadingTypeRepositoryImpl(DBConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    /**
     * Adds a column to the readings table in the database.
     * It executes an SQL ALTER TABLE statement to add the column.
     *
     * @param columnName the name of the column to add.
     */
    public void addColumnToReadingsTable(String columnName) {
        String sql = "ALTER TABLE develop.readings ADD COLUMN " + columnName + " double precision";
        executeUpdate(sql);
    }

    /**
     * Removes a column from the readings table in the database.
     * It executes an SQL ALTER TABLE statement to remove the column.
     *
     * @param columnName the name of the column to remove.
     */
    public void removeColumnFromReadingsTable(String columnName) {
        String sql = "ALTER TABLE develop.readings DROP COLUMN " + columnName;
        executeUpdate(sql);
    }

    /**
     * Gets the reading types from the database.
     * It gets the column names from the readings table and returns them as a list.
     * It excludes the id, username, month, and year columns.
     * If there is an error getting the column names, it throws a DatabaseException.
     *
     * @return a list of the reading types.
     * @throws RuntimeException if there is an error getting the column names.
     */
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
            throw new RuntimeException("Failed to get reading types from database: " + e.getMessage());
        }
        return readingTypes;
    }
}