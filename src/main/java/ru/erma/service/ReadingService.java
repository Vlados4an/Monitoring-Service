package ru.erma.service;

import lombok.RequiredArgsConstructor;
import ru.erma.handler.HandlerDependencies;
import ru.erma.model.Reading;
import ru.erma.repository.ReadingRepository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This class provides services related to Reading operations.
 * It uses a ReadingRepository to perform operations on Reading data.
 */
@RequiredArgsConstructor
public class ReadingService {

    private final ReadingRepository<String, Reading> readingRepository;
    private final ReadingStructureService readingStructureService;

    /**
     * Submits readings for a user for a specific month and year.
     * If readings for the month have already been submitted, prints a message and returns.
     *
     * @param username the username of the user
     * @param month the month of the readings
     * @param year the year of the readings
     * @param values the readings to submit
     */
    public void submitReadings(String username, int month, int year, Map<String, Integer> values) {
        List<Reading> userReadings = readingRepository.findByUsername(username);

        for (Reading reading : userReadings) {
            if (reading.getMonth() == month && reading.getYear() == year) {
                System.out.println("Readings for this month already submitted.");
                return;
            }
        }

        Map<String, Integer> newValues = new HashMap<>();
        for (String type : readingStructureService.getReadingTypes()) {
            newValues.put(type, values.getOrDefault(type, null));
        }

        Reading newReading = new Reading(month, year, newValues);
        readingRepository.save(username, newReading);
    }


    /**
     * Retrieves all readings for a user for a specific month and year.
     *
     * @param username the username of the user
     * @param month the month of the readings
     * @param year the year of the readings
     * @return a list of readings for the specified username, month, and year
     */
    public List<Reading> getReadingsForMonth(String username,int month,int year){
        return readingRepository.findByUsernameAndMonthAndYear(username, month, year);
    }

    /**
     * Retrieves the reading history for a user.
     *
     * @param username the username of the user
     * @return a list of all readings for the specified username
     */
    public List<Reading> getReadingHistory(String username) {
        return readingRepository.findByUsername(username);
    }


    /**
     * Retrieves the most recent readings for a user.
     *
     * @param username the username of the user
     * @return the most recent Reading object for the specified username, or null if no readings exist
     */
    public Reading getActualReadings(String username) {
        List<Reading> userReadings = readingRepository.findByUsername(username);
        if (userReadings != null && !userReadings.isEmpty()) {
            userReadings.sort(Comparator.comparing(Reading::getYear).thenComparing(Reading::getMonth));
            return userReadings.get(userReadings.size() - 1);
        }

        return null;
    }
}

