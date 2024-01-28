package ru.erma.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.erma.model.Reading;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class contains unit tests for the ReadingRepositoryImpl class.
 */
class ReadingRepositoryImplTest {
    private ReadingRepositoryImpl readingRepository;

    /**
     * This method is executed before each test.
     * It initializes the readingRepository instance.
     */
    @BeforeEach
    public void setUp(){
        readingRepository = new ReadingRepositoryImpl();
    }

    /**
     * This test verifies that the save method of ReadingRepositoryImpl adds a reading to the user's readings.
     */
    @Test
    void save_addsReadingToUserReadings() {
        Reading reading = new Reading();
        reading.setMonth(1);
        reading.setYear(2022);
        readingRepository.save("testUser",reading);

        List<Reading> userReadings = readingRepository.findByUsername("testUser");

        assertThat(userReadings).hasSize(1);
        assertThat(userReadings.get(0)).usingRecursiveComparison().isEqualTo(reading);
    }

    /**
     * This test verifies that the findByUsername method of ReadingRepositoryImpl returns an empty list when the user has no readings.
     */
    @Test
    void findByUsername_returnsEmptyListIfNoReadings() {
        List<Reading> userReadings = readingRepository.findByUsername("nonExistentUser");

        assertThat(userReadings).isEmpty();
    }

    /**
     * This test verifies that the findByUsernameAndMonthAndYear method of ReadingRepositoryImpl returns readings for a specific month and year.
     */
    @Test
    void findByUsernameForMonth_returnsReadingsForSpecificMonth(){
        Reading reading1 = new Reading();
        reading1.setMonth(1);
        reading1.setYear(2022);
        readingRepository.save("testUser",reading1);

        Reading reading2 = new Reading();
        reading2.setMonth(2);
        reading2.setYear(2022);
        readingRepository.save("testUser",reading2);

        List<Reading> userReadings = readingRepository.findByUsernameAndMonthAndYear("testUser",1,2022);

        assertThat(userReadings).hasSize(1);
        assertThat(userReadings.get(0)).usingRecursiveComparison().isEqualTo(reading1);
    }

    /**
     * This test verifies that the findByUsernameAndMonthAndYear method of ReadingRepositoryImpl returns an empty list when the user has no readings for a specific month and year.
     */
    @Test
    void findByUsernameForMonth_returnsEmptyListIfNoReadingsForMonth() {
        Reading reading = new Reading();
        reading.setMonth(1);
        reading.setYear(2022);
        readingRepository.save("testUser",reading);

        List<Reading> userReadings = readingRepository.findByUsernameAndMonthAndYear("testUser",2,2022);
        assertThat(userReadings).isEmpty();
    }
}