package ru.erma.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.erma.repository.ReadingTypeRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * The ReadingStructureServiceTest class tests the functionality of the ReadingStructureService class.
 * It uses Mockito to create a mock ReadingTypeRepository for testing.
 */

class ReadingStructureServiceTest {
    private ReadingStructureService readingStructureService;
    /**
     * The setUp method initializes the ReadingStructureService instance before each test.
     * It creates a mock ReadingTypeRepository and passes it to the ReadingStructureService constructor.
     */

    @BeforeEach
    void setUp() {
        ReadingTypeRepository readingTypeRepository = mock(ReadingTypeRepository.class);
        readingStructureService = new ReadingStructureService(readingTypeRepository);
    }
    /**
     * This test checks that the addReadingType method correctly adds a new type to the list.
     * It adds a type to the list and then asserts that the list contains the added type.
     */
    @Test
    @DisplayName("AddReadingType method adds a new type to the list")
    void addReadingType_addsNewTypeToList() {
        readingStructureService.addReadingType("gas");

        assertThat(readingStructureService.getReadingTypes()).contains("gas");
    }
    /**
     * This test checks that the removeReadingType method correctly removes an existing type from the list.
     * It removes a type from the list and then asserts that the list does not contain the removed type.
     */
    @Test
    @DisplayName("RemoveReadingType method removes an existing type from the list")
    void removeReadingType_removesTypeFromList() {
        readingStructureService.removeReadingType("heating");
        assertThat(readingStructureService.getReadingTypes()).doesNotContain("heating");
    }
}