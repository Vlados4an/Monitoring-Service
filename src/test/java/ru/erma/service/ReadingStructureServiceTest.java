package ru.erma.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.erma.repository.ReadingTypeRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ReadingStructureServiceTest {
    private ReadingStructureService readingStructureService;

    @BeforeEach
    void setUp() {
        ReadingTypeRepository readingTypeRepository = mock(ReadingTypeRepository.class);
        readingStructureService = new ReadingStructureService(readingTypeRepository);
    }

    @Test
    @DisplayName("AddReadingType method adds a new type to the list")
    void addReadingType_addsNewTypeToList() {
        readingStructureService.addReadingType("gas");

        assertThat(readingStructureService.getReadingTypes()).contains("gas");
    }

    @Test
    @DisplayName("RemoveReadingType method removes an existing type from the list")
    void removeReadingType_removesTypeFromList() {
        readingStructureService.removeReadingType("heating");
        assertThat(readingStructureService.getReadingTypes()).doesNotContain("heating");
    }
}