package ru.erma.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.erma.exception.DatabaseException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ReadingTypeRepositoryImplTest extends AbstractRepositoryForTest {
    private ReadingTypeRepositoryImpl readingTypeRepository;

    @BeforeEach
    void setUp() {
        super.setUp();
        readingTypeRepository = new ReadingTypeRepositoryImpl(connectionProvider);
    }

    @Test
    void shouldAddColumnToReadingsTable() {
        assertThatCode(() -> readingTypeRepository.addColumnToReadingsTable("testColumn"))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldThrowExceptionWhenAddingExistingColumn() {
        readingTypeRepository.addColumnToReadingsTable("existingColumn");
        assertThatThrownBy(() -> readingTypeRepository.addColumnToReadingsTable("existingColumn"))
                .isInstanceOf(DatabaseException.class);
    }

    @Test
    void shouldRemoveColumnFromReadingsTable() {
        readingTypeRepository.addColumnToReadingsTable("testColumn");
        assertThatCode(() -> readingTypeRepository.removeColumnFromReadingsTable("testColumn"))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldThrowExceptionWhenRemovingNonExistingColumn() {
        assertThatThrownBy(() -> readingTypeRepository.removeColumnFromReadingsTable("nonExistingColumn"))
                .isInstanceOf(DatabaseException.class);
    }
}