package ru.erma.repository;

import java.util.List;

public interface ReadingTypeRepository<E> {
    void addColumnToReadingsTable(E columnName);
    void removeColumnFromReadingsTable(E columnName);
    List<E> getReadingTypesFromDatabase();
}
