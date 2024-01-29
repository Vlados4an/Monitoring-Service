package ru.erma.repository.impl;

import ru.erma.model.Reading;
import ru.erma.repository.ReadingRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
        * This class provides an implementation of the ReadingRepository interface.
        * It uses a HashMap to store Reading objects for each user, using their username as the key.
        */
public class ReadingRepositoryImpl implements ReadingRepository<String,Reading> {
    private final Map<String, List<Reading>> readings = new HashMap<>();

    /**
     * Saves the given Reading object for the specified username.
     * The Reading is added to the list of readings for the user in the readings map.
     *
     * @param username the username for which to save the reading
     * @param reading the Reading object to save
     */
    @Override
    public void save(String username, Reading reading) {
        List<Reading> userReadings = readings.getOrDefault(username, new ArrayList<>());
        userReadings.add(reading);
        readings.put(username, userReadings);
    }

    /**
     * Retrieves all Reading objects for the specified username.
     *
     * @param username the username for which to retrieve the readings
     * @return a list of Reading objects for the specified username
     */
    @Override
    public List<Reading> findByUsername(String username) {
        return readings.getOrDefault(username, new ArrayList<>());
    }

    /**
     * Retrieves all Reading objects for the specified username, month, and year.
     *
     * @param username the username for which to retrieve the readings
     * @param month the month for which to retrieve the readings
     * @param year the year for which to retrieve the readings
     * @return a list of Reading objects for the specified username, month, and year
     */

    @Override
    public List<Reading> findByUsernameAndMonthAndYear(String username, int month, int year) {
        List<Reading> userReadings = findByUsername(username);
        List<Reading> readingsForMonth = new ArrayList<>();
        for (Reading reading : userReadings) {
            if (reading.getMonth() == month && reading.getYear() == year) {
                readingsForMonth.add(reading);
            }
        }
        return readingsForMonth;
    }

}
