package ru.erma.repository;

import java.util.List;

/**
 * The ReadingTypeRepository interface defines the operations that can be performed on the reading types in the database.
 * It provides methods to add and remove columns from the readings table, and to get the reading types from the database.
 *
 * @param <E> the type of the reading types.
 */
public interface ReadingTypeRepository<E> {

    /**
     * Adds a column to the readings table in the database.
     *
     * @param columnName the name of the column to add.
     */
    void addColumnToReadingsTable(E columnName);

    /**
     * Removes a column from the readings table in the database.
     *
     * @param columnName the name of the column to remove.
     */
    void removeColumnFromReadingsTable(E columnName);

    /**
     * Gets the reading types from the database.
     *
     * @return a list of the reading types.
     */
    List<E> getReadingTypesFromDatabase();
}