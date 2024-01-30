package ru.erma.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class is used to test the ReadingStructureService class.
 */
class ReadingStructureServiceTest {
    private ReadingStructureService readingStructureService;

    /**
     * This method is executed before each test.
     * It initializes the object under test.
     */
    @BeforeEach
    void setUp() {
        readingStructureService = new ReadingStructureService();
    }

    /**
     * This test verifies that when the addReadingType method is called,
     * it adds a new type to the list of reading types.
     */
    @Test
    @DisplayName("AddReadingType method adds a new type to the list")
    void addReadingType_addsNewTypeToList() {
        readingStructureService.addReadingType("gas");

        assertThat(readingStructureService.getReadingTypes()).contains("gas");
    }

    /**
     * This test verifies that when the removeReadingType method is called with an existing type,
     * it removes the type from the list of reading types.
     */
    @Test
    @DisplayName("RemoveReadingType method removes an existing type from the list")
    void removeReadingType_removesTypeFromList() {
        readingStructureService.removeReadingType("heating");

        assertThat(readingStructureService.getReadingTypes()).doesNotContain("heating");
    }

    /**
     * This test verifies that when the removeReadingType method is called with a type that does not exist,
     * it returns false.
     */
    @Test
    @DisplayName("RemoveReadingType method returns false when type does not exist")
    void removeReadingType_returnsFalseWhenTypeDoesNotExist() {
        boolean result = readingStructureService.removeReadingType("gas");

        assertThat(result).isFalse();
    }

    /**
     * This test verifies that when the removeReadingType method is called with a type that exists,
     * it returns true.
     */
    @Test
    @DisplayName("RemoveReadingType method returns true when type exists")
    void removeReadingType_returnsTrueWhenTypeExists() {
        boolean result = readingStructureService.removeReadingType("heating");

        assertThat(result).isTrue();
    }
}