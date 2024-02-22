package ru.erma.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.erma.dto.ReadingDTO;
import ru.erma.dto.ReadingListDTO;
import ru.erma.dto.ReadingRequest;
import ru.erma.exception.ReadingNotFoundException;
import ru.erma.mappers.ReadingMapper;
import ru.erma.model.Reading;
import ru.erma.repository.ReadingRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    /**
     * The ReadingService instance under test, with mocked dependencies.
     */

    @Mock
    private ReadingMapper readingMapper;

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

        Reading reading = new Reading();
        when(readingMapper.toReading(readingRequest)).thenReturn(reading);


        readingService.submitReadings(readingRequest);

        verify(readingRepository, times(1)).save(eq("testUser"), eq(reading));
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

        ReadingListDTO readingListDTO = new ReadingListDTO();
        when(readingMapper.toReadingListDTO(List.of(reading1))).thenReturn(readingListDTO);

        ReadingListDTO userReadings = readingService.getReadingsForMonth("testUser", 1, 2022);

        assertThat(userReadings).isNotNull();
        assertThat(userReadings).isEqualTo(readingListDTO);
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

        List<Reading> readings = List.of(reading1);
        when(readingRepository.findByUsername("testUser")).thenReturn(readings);

        ReadingListDTO readingListDTO = new ReadingListDTO();
        when(readingMapper.toReadingListDTO(readings)).thenReturn(readingListDTO);

        ReadingListDTO userReadings = readingService.getReadingHistory("testUser");

        assertThat(userReadings).isNotNull();
        assertThat(userReadings).isEqualTo(readingListDTO);
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
        Reading reading = new Reading();
        reading.setMonth(1);
        reading.setYear(2022);

        ReadingDTO readingDTO = new ReadingDTO();
        readingDTO.setMonth(1);
        readingDTO.setYear(2022);

        when(readingRepository.findLatestByUsername("testUser")).thenReturn(Optional.of(reading));
        when(readingMapper.toReadingDTO(reading)).thenReturn(readingDTO);

        ReadingDTO actualReading = readingService.getActualReadings("testUser");

        assertThat(actualReading).isNotNull();
        assertThat(actualReading).usingRecursiveComparison().isEqualTo(readingDTO);
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

        assertThatThrownBy(() -> readingService.getActualReadings(username))
                .isInstanceOf(ReadingNotFoundException.class);
    }
}