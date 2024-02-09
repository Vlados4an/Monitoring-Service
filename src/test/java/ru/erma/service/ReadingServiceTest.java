package ru.erma.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.erma.dto.ReadingRequest;
import ru.erma.exception.NotValidArgumentException;
import ru.erma.exception.ReadingNotFoundException;
import ru.erma.model.Reading;
import ru.erma.repository.ReadingRepository;
import ru.erma.service.ReadingService;
import ru.erma.service.ReadingStructureService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

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

    @Mock
    private ReadingStructureService readingStructureService;

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
        ReadingRequest readingRequest = new ReadingRequest("testUser", 1, 2022, values);

        when(readingStructureService.getReadingTypes()).thenReturn(Arrays.asList("heating", "cold water", "hot water"));

        readingService.submitReadings(readingRequest);

        verify(readingRepository, times(1)).save(eq("testUser"), any(Reading.class));
    }

    /**
     * This test verifies that the submitReadings method throws a NotValidArgumentException
     * when an invalid month is provided in the ReadingRequest.
     * The ReadingRequest is created with a month value of 13, which is invalid because the valid range for month is 1-12.
     * The test asserts that a NotValidArgumentException is thrown when submitReadings is called with this ReadingRequest.
     */
    @Test
    @DisplayName("Submit readings throws exception for invalid month")
    void submitReadings_throwsExceptionForInvalidMonth() {
        Map<String, Integer> values = new HashMap<>();
        values.put("heating", 20);
        values.put("cold water", 30);
        values.put("hot water", 40);
        ReadingRequest readingRequest = new ReadingRequest("testUser", 13, 2022, values);

        assertThatThrownBy(() -> readingService.submitReadings(readingRequest))
                .isInstanceOf(NotValidArgumentException.class);
    }

    /**
     * This test verifies that the submitReadings method throws a NotValidArgumentException
     * when an invalid year is provided in the ReadingRequest.
     * The ReadingRequest is created with a year value of -1, which is invalid because the year should be a positive number.
     * The test asserts that a NotValidArgumentException is thrown when submitReadings is called with this ReadingRequest.
     */
    @Test
    @DisplayName("Submit readings throws exception for invalid year")
    void submitReadings_throwsExceptionForInvalidYear() {
        Map<String, Integer> values = new HashMap<>();
        values.put("heating", 20);
        values.put("cold water", 30);
        values.put("hot water", 40);
        ReadingRequest readingRequest = new ReadingRequest("testUser", 1, -1, values);

        assertThatThrownBy(() -> readingService.submitReadings(readingRequest))
                .isInstanceOf(NotValidArgumentException.class);
    }

    /**
     * This test verifies that the submitReadings method throws a NotValidArgumentException
     * when an invalid reading value is provided in the ReadingRequest.
     * The ReadingRequest is created with a reading value of -20 for "heating", which is invalid because reading values should be positive numbers.
     * The test asserts that a NotValidArgumentException is thrown when submitReadings is called with this ReadingRequest.
     */
    @Test
    @DisplayName("Submit readings throws exception for invalid readings")
    void submitReadings_throwsExceptionForInvalidReadings() {
        Map<String, Integer> values = new HashMap<>();
        values.put("heating", -20);
        values.put("cold water", 30);
        values.put("hot water", 40);
        ReadingRequest readingRequest = new ReadingRequest("testUser", 1, 2022, values);

        assertThatThrownBy(() -> readingService.submitReadings(readingRequest))
                .isInstanceOf(NotValidArgumentException.class);
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
     * This test verifies that the getReadingsForMonth method throws a ReadingNotFoundException
     * when no readings are found for the specified month and year.
     * The test sets up the readingRepository mock to return an empty list when findByUsernameAndMonthAndYear is called.
     * It then asserts that a ReadingNotFoundException is thrown when getReadingsForMonth is called with these parameters.
     */
    @Test
    @DisplayName("Get readings for month throws exception when no readings found")
    void getReadingsForMonth_throwsExceptionWhenNoReadingsFound() {
        String username = "testUser";
        int month = 1;
        int year = 2022;

        when(readingRepository.findByUsernameAndMonthAndYear(username, month, year)).thenReturn(List.of());

        assertThatThrownBy(() -> readingService.getReadingsForMonth(username, month, year))
                .isInstanceOf(ReadingNotFoundException.class);
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
     * This test verifies that the getReadingHistory method throws a ReadingNotFoundException
     * when no readings are found for a user.
     * The test sets up the readingRepository mock to return an empty list when findByUsername is called.
     * It then asserts that a ReadingNotFoundException is thrown when getReadingHistory is called with this username.
     */
    @Test
    @DisplayName("Get reading history throws exception when no readings found")
    void getReadingHistory_throwsExceptionWhenNoReadingsFound() {
        String username = "testUser";

        when(readingRepository.findByUsername(username)).thenReturn(List.of());

        assertThatThrownBy(() -> readingService.getReadingHistory(username))
                .isInstanceOf(ReadingNotFoundException.class);
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

    /**
     * This test verifies that the getActualReadings method throws a ReadingNotFoundException
     * when no readings are found for a user.
     * The test sets up the readingRepository mock to return an empty list when findByUsername is called.
     * It then asserts that a ReadingNotFoundException is thrown when getActualReadings is called with this username.
     */
    @Test
    @DisplayName("Get actual readings throws exception when no readings found")
    void getActualReadings_throwsExceptionWhenNoReadingsFound() {
        String username = "testUser";
        when(readingRepository.findByUsername(username)).thenReturn(List.of());

        assertThatThrownBy(() -> readingService.getActualReadings(username))
                .isInstanceOf(ReadingNotFoundException.class);
    }
}