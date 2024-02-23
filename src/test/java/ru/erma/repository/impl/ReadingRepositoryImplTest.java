package ru.erma.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.erma.config.AbstractTestContainerConfig;
import ru.erma.model.Reading;
import ru.erma.repository.ReadingRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * The ReadingRepositoryImplTest class tests the functionality of the ReadingRepositoryImpl class.
 * It extends the AbstractRepositoryForTest class to reuse its setup logic.
 */
public class ReadingRepositoryImplTest extends AbstractTestContainerConfig {
    private ReadingRepository<String,Reading> readingRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Sets up the ReadingRepositoryImpl instance before each test.
     * Calls the setUp method of the superclass to initialize the JdbcTemplate,
     * and then creates a new AuditRepositoryImpl with the JdbcTemplate.
     */
    @BeforeEach
    void setUp() {
        readingRepository = new ReadingRepositoryImpl(jdbcTemplate);
    }

    /**
     * This test checks that the save method correctly saves a reading to the database.
     * It creates a reading, sets its month and year, and then saves it to the database.
     * It asserts that no exception is thrown when saving the reading.
     */
    @Test
    @DisplayName("Test that reading is saved correctly")
    void shouldSaveReading() {
        Map<String,Integer> values = new HashMap<>();
        Reading reading = new Reading();
        reading.setMonth(1);
        reading.setYear(2022);
        reading.setValues(values);


        assertThatCode(() -> readingRepository.save("test_user", reading)).doesNotThrowAnyException();
    }

    /**
     * This test checks that the save method correctly handles the case where the reading is null.
     * It attempts to save a null reading to the database.
     * It asserts that a DatabaseException is thrown.
     */
    @Test
    @DisplayName("Test that exception is thrown when saving null reading")
    void shouldThrowExceptionWhenSavingNullReading() {
        assertThatThrownBy(() -> readingRepository.save("test_user", null))
                .isInstanceOf(RuntimeException.class);
    }

    /**
     * This test checks that the findByUsername method correctly retrieves all readings for a specific username from the database.
     * It retrieves the readings for a username and asserts that the list is not empty and that the first reading's month, year, and values match the expected values.
     */
    @Test
    @DisplayName("Test that readings are retrieved correctly by username")
    void shouldFindByUsername() {
        List<Reading> readings = readingRepository.findByUsername("test_user");

        assertThat(readings).isNotEmpty();
        Reading reading = readings.get(0);
        assertThat(reading.getMonth()).isEqualTo(1);
        assertThat(reading.getYear()).isEqualTo(2022);
        assertThat(reading.getValues().get("heating")).isEqualTo(100);
        assertThat(reading.getValues().get("cold_water")).isEqualTo(200);
        assertThat(reading.getValues().get("hot_water")).isEqualTo(300);
    }

    /**
     * This test checks that the findByUsername method correctly handles the case where there are no readings for a specific username in the database.
     * It attempts to retrieve the readings for a username that does not exist in the database.
     * It asserts that the returned list of readings is empty.
     */
    @Test
    @DisplayName("Test that empty list is returned when no readings for username")
    void shouldReturnEmptyListWhenNoReadingsForUsername() {
        List<Reading> readings = readingRepository.findByUsername("nonExistingUser");

        assertThat(readings).isEmpty();
    }

    /**
     * This test checks that the findByUsernameAndMonthAndYear method correctly retrieves all readings for a specific username, month, and year from the database.
     * It retrieves the readings for a username, month, and year and asserts that the list is not empty and that the first reading's month, year, and values match the expected values.
     */
    @Test
    @DisplayName("Test that readings are retrieved correctly by username, month and year")
    void shouldFindByUsernameAndMonthAndYear() {
        List<Reading> readings = readingRepository.findByUsernameAndMonthAndYear("test_user", 1, 2022);

        assertThat(readings).isNotEmpty();
        Reading reading = readings.get(0);
        assertThat(reading.getMonth()).isEqualTo(1);
        assertThat(reading.getYear()).isEqualTo(2022);
        assertThat(reading.getValues().get("heating")).isEqualTo(100);
        assertThat(reading.getValues().get("cold_water")).isEqualTo(200);
        assertThat(reading.getValues().get("hot_water")).isEqualTo(300);
    }

    /**
     * This test checks that the findByUsernameAndMonthAndYear method correctly handles the case where there are no readings for a specific username, month, and year in the database.
     * It attempts to retrieve the readings for a username, month, and year that do not exist in the database.
     * It asserts that the returned list of readings is empty.
     */
    @Test
    @DisplayName("Test that empty list is returned when no readings for username, month and year")
    void shouldReturnEmptyListWhenNoReadingsForUsernameAndMonthAndYear() {
        List<Reading> readings = readingRepository.findByUsernameAndMonthAndYear("nonExistingUser", 1, 2022);

        assertThat(readings).isEmpty();
    }

    /**
     * Tests that the findLatestByUsername method correctly retrieves the latest reading for a specific username from the database.
     * Retrieves the latest reading for a username and asserts that the Optional is not empty and that the reading's month, year, and values match the expected values.
     */
    @Test
    @DisplayName("Reading is retrieved correctly when latest reading for username exists")
    void shouldFindLatestByUsernameWhenReadingExists() {
        Optional<Reading> result = readingRepository.findLatestByUsername("test_user");
        Reading reading = result.get();

        assertThat(result).isNotEmpty();
        assertThat(reading.getMonth()).isEqualTo(1);
        assertThat(reading.getYear()).isEqualTo(2022);
        assertThat(reading.getValues().get("heating")).isEqualTo(100);
        assertThat(reading.getValues().get("cold_water")).isEqualTo(200);
        assertThat(reading.getValues().get("hot_water")).isEqualTo(300);
    }

    /**
     * Tests that the findLatestByUsername method correctly handles the case where there is no latest reading for a specific username in the database.
     * Attempts to retrieve the latest reading for a username that does not exist in the database.
     * Asserts that the returned Optional is empty.
     */
    @Test
    @DisplayName("Empty optional is returned when no latest reading for username")
    void shouldReturnEmptyOptionalWhenNoLatestReadingForUsername() {
        Optional<Reading> result = readingRepository.findLatestByUsername("nonExistingUser");

        assertThat(result).isEmpty();
    }
}