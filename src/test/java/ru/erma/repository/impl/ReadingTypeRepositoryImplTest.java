package ru.erma.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.erma.config.AbstractTestContainerConfig;
import ru.erma.repository.ReadingTypeRepository;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * This class is responsible for testing the ReadingTypeRepositoryImpl class.
 * It extends AbstractTestContainerConfig to use a PostgreSQL test container.
 */
public class ReadingTypeRepositoryImplTest extends AbstractTestContainerConfig {

    @Autowired
    private ReadingTypeRepository<String> readingTypeRepository;

    /**
     * This test checks that the addColumnToReadingsTable method correctly adds a column to the readings table.
     * It attempts to add a column to the readings table and asserts that no exception is thrown.
     */
    @Test
    @DisplayName("Test that column is added correctly to readings table")
    void shouldAddColumnToReadingsTable() {
        assertThatCode(() -> readingTypeRepository.addColumnToReadingsTable("testColumn"))
                .doesNotThrowAnyException();
    }

    /**
     * This test checks that the addColumnToReadingsTable method correctly handles the case where the column already exists.
     * It adds a column to the readings table and then attempts to add the same column again.
     * It asserts that a DatabaseException is thrown.
     */
    @Test
    @DisplayName("Test that exception is thrown when adding existing column")
    void shouldThrowExceptionWhenAddingExistingColumn() {
        readingTypeRepository.addColumnToReadingsTable("existingColumn");
        assertThatThrownBy(() -> readingTypeRepository.addColumnToReadingsTable("existingColumn"))
                .isInstanceOf(RuntimeException.class);
    }

    /**
     * This test checks that the removeColumnFromReadingsTable method correctly removes a column from the readings table.
     * It adds a column to the readings table, removes it, and asserts that no exception is thrown.
     */
    @Test
    @DisplayName("Test that column is removed correctly from readings table")
    void shouldRemoveColumnFromReadingsTable() {
        readingTypeRepository.addColumnToReadingsTable("testColumn");
        assertThatCode(() -> readingTypeRepository.removeColumnFromReadingsTable("testColumn"))
                .doesNotThrowAnyException();
    }

    /**
     * This test checks that the removeColumnFromReadingsTable method correctly handles the case where the column does not exist.
     * It attempts to remove a column that does not exist from the readings table.
     * It asserts that a DatabaseException is thrown.
     */
    @Test
    @DisplayName("Test that exception is thrown when removing non-existing column")
    void shouldThrowExceptionWhenRemovingNonExistingColumn() {
        assertThatThrownBy(() -> readingTypeRepository.removeColumnFromReadingsTable("nonExistingColumn"))
                .isInstanceOf(RuntimeException.class);
    }
}