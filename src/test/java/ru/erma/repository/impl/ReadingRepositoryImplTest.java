package ru.erma.repository.impl;

import ru.erma.exception.DatabaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.erma.model.Reading;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ReadingRepositoryImplTest extends AbstractRepositoryForTest {
    private ReadingRepositoryImpl readingRepository;

    @BeforeEach
    void setUp() {
        super.setUp();
        readingRepository = new ReadingRepositoryImpl(connectionProvider);
    }

    @Test
    void shouldSaveReading() {
        Map<String,Integer> values = new HashMap<>();
        Reading reading = new Reading();
        reading.setMonth(1);
        reading.setYear(2022);
        reading.setValues(values);


        assertThatCode(() -> readingRepository.save("test_user", reading)).doesNotThrowAnyException();
    }

    @Test
    void shouldThrowExceptionWhenSavingNullReading() {
        assertThatThrownBy(() -> readingRepository.save("test_user", null))
                .isInstanceOf(DatabaseException.class);
    }

    @Test
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

    @Test
    void shouldReturnEmptyListWhenNoReadingsForUsername() {
        List<Reading> readings = readingRepository.findByUsername("nonExistingUser");

        assertThat(readings).isEmpty();
    }

    @Test
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

    @Test
    void shouldReturnEmptyListWhenNoReadingsForUsernameAndMonthAndYear() {
        List<Reading> readings = readingRepository.findByUsernameAndMonthAndYear("nonExistingUser", 1, 2022);

        assertThat(readings).isEmpty();
    }
}