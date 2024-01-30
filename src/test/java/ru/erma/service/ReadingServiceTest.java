package ru.erma.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.erma.model.Reading;
import ru.erma.repository.ReadingRepository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * This class is used to test the ReadingService class.
 * It uses the Mockito framework for mocking objects and JUnit for running the tests.
 */
@ExtendWith(MockitoExtension.class)
class ReadingServiceTest {

    /**
     * Mock of ReadingRepository used in the tests.
     */
    @Mock
    private ReadingRepository<String,Reading> readingRepository;

    /**
     * The ReadingService instance under test, with mocked dependencies.
     */
    @InjectMocks
    private ReadingService readingService;

    /**
     * This test verifies that when the submitReadings method is called,
     * it adds a new Reading to the user's readings.
     */
    @Test
    @DisplayName("Submit readings adds a reading to user readings")
    void submitReadings_addsReadingToUserReadings() {
        Map<String, Integer> values = new HashMap<>();
        values.put("heating", 20);
        values.put("cold water", 30);
        values.put("hot water", 40);

        readingService.submitReadings("testUser", 1, 2022, values);

        verify(readingRepository, times(1)).save(eq("testUser"), any(Reading.class));
    }

    /**
     * This test verifies that when the getReadingsForMonth method is called,
     * it returns all Readings for a specific month and year for the user.
     */
    @Test
    @DisplayName("Get readings for month returns readings for specific month and year")
    void getReadingsForMonth_returnsReadingsForSpecificMonthAndYear() {
        Reading reading1 = new Reading();
        reading1.setMonth(1);
        reading1.setYear(2022);
        when(readingRepository.findByUsernameAndMonthAndYear("testUser", 1, 2022)).thenReturn(List.of(reading1));

        List<Reading> userReadings = readingService.getReadingsForMonth("testUser", 1, 2022);

        assertThat(userReadings).hasSize(1);
        assertThat(userReadings.get(0)).usingRecursiveComparison().isEqualTo(reading1);
    }

    /**
     * This test verifies that when the getReadingHistory method is called,
     * it returns all Readings for the user.
     */
    @Test
    @DisplayName("Get reading history returns all readings for user")
    void getReadingHistory_returnsAllReadingsForUser() {
        Reading reading1 = new Reading();
        reading1.setMonth(1);
        reading1.setYear(2022);
        Reading reading2 = new Reading();
        reading2.setMonth(2);
        reading2.setYear(2022);
        when(readingRepository.findByUsername("testUser")).thenReturn(Arrays.asList(reading1, reading2));

        List<Reading> userReadings = readingService.getReadingHistory("testUser");

        assertThat(userReadings).hasSize(2);
        assertThat(userReadings.get(0)).usingRecursiveComparison().isEqualTo(reading1);
        assertThat(userReadings.get(1)).usingRecursiveComparison().isEqualTo(reading2);
    }

    /**
     * This test verifies that when the getActualReadings method is called,
     * it returns the most recent Reading for the user.
     */
    @Test
    @DisplayName("Get actual readings returns most recent reading for user")
    void getActualReadings_returnsMostRecentReadingForUser() {
        Reading reading1 = new Reading();
        reading1.setMonth(1);
        reading1.setYear(2022);
        Reading reading2 = new Reading();
        reading2.setMonth(2);
        reading2.setYear(2022);
        when(readingRepository.findByUsername("testUser")).thenReturn(Arrays.asList(reading1, reading2));

        Reading actualReading = readingService.getActualReadings("testUser");

        assertThat(actualReading).usingRecursiveComparison().isEqualTo(reading2);
    }
}