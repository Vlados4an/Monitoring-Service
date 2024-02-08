package ru.erma.service;

import ru.erma.aop.annotations.Audit;
import ru.erma.dto.ReadingRequest;
import ru.erma.exception.NotValidArgumentException;
import ru.erma.exception.ReadingAlreadyExistsException;
import ru.erma.exception.ReadingNotFoundException;
import ru.erma.mappers.ReadingMapper;
import ru.erma.model.Reading;
import ru.erma.repository.ReadingRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;


/**
 * The ReadingService class provides services related to Reading operations.
 * It uses a ReadingRepository to perform operations on Reading data.
 * It also uses a ReadingStructureService to manage the structure of readings.
 */
public class ReadingService {

    private final ReadingRepository<String, Reading> readingRepository;
    private final ReadingStructureService readingStructureService;

    /**
     * Constructs a new ReadingService instance with the specified ReadingRepository and ReadingStructureService.
     *
     * @param readingRepository the ReadingRepository used to perform operations on Reading data
     * @param readingStructureService the ReadingStructureService used to manage the structure of readings
     */
    public ReadingService(ReadingRepository<String, Reading> readingRepository, ReadingStructureService readingStructureService) {
        this.readingRepository = readingRepository;
        this.readingStructureService = readingStructureService;
    }

    /**
     * Submits readings for a user.
     * It validates the input, checks if readings for the specified month and year already exist, and saves the new readings.
     * If readings for the specified month and year already exist, it throws a ReadingAlreadyExistsException.
     * If the input is not valid, it throws a NotValidArgumentException.
     *
     * @param request the ReadingRequest containing the readings to be submitted
     */
    @Audit(action = "User submitted readings: ")
    public void submitReadings(ReadingRequest request) {
        validateInput(request.month(), request.year(), request.values());
        List<Reading> userReadings = readingRepository.findByUsername(request.username());

        for (Reading reading : userReadings) {
            if (reading.getMonth().equals(request.month()) && reading.getYear().equals(request.year())) {
                throw new ReadingAlreadyExistsException("Readings for this month already submitted.");
            }
        }

        Reading newReading = ReadingMapper.INSTANCE.toReading(request);
        readingRepository.save(request.username(), newReading);
    }

    /**
     * Retrieves all readings for a user for a specific month and year.
     *
     * @param username the username of the user
     * @param month the month of the readings
     * @param year the year of the readings
     * @return a list of readings for the specified username, month, and year
     */
    @Audit(action = "User viewed readings for month: ")
    public List<Reading> getReadingsForMonth(String username,Integer month,Integer year){
        List<Reading> userReadings = readingRepository.findByUsernameAndMonthAndYear(username, month, year);
        if (userReadings == null || userReadings.isEmpty()) {
            throw new ReadingNotFoundException("No readings found for user with username " + username + " for month " + month + " and year " + year);
        }
        return userReadings;
    }

    /**
     * Retrieves the reading history for a user.
     *
     * @param username the username of the user
     * @return a list of all readings for the specified username
     */
    @Audit(action = "User viewed reading history: ")
    public List<Reading> getReadingHistory(String username) {
        return getUserReadings(username);
    }


    /**
     * Retrieves the most recent readings for a user.
     *
     * @param username the username of the user
     * @return the most recent Reading object for the specified username, or null if no readings exist
     */
    @Audit(action = "User viewed actual readings: ")
    public Reading getActualReadings(String username) {
        List<Reading> userReadings = getUserReadings(username);
        userReadings.sort(Comparator.comparing(Reading::getYear).thenComparing(Reading::getMonth));

        return userReadings.get(userReadings.size() - 1);
    }

    /**
     * Retrieves all readings for a user.
     * If no readings are found, it throws a ReadingNotFoundException.
     *
     * @param username the username of the user
     * @return a list of all readings for the specified username
     */
    private List<Reading> getUserReadings(String username) {
        List<Reading> userReadings = readingRepository.findByUsername(username);
        if (userReadings == null || userReadings.isEmpty()) {
            throw new ReadingNotFoundException("No readings found for user with username " + username);
        }
        return userReadings;
    }
    /**
     * Validates the input for submitting readings.
     * It checks if the month is between 1 and 12, if the year is a positive number, and if the readings are positive numbers.
     * If the input is not valid, it throws a NotValidArgumentException.
     *
     * @param month the month of the readings
     * @param year the year of the readings
     * @param values a map of reading types and their corresponding values
     */
    private void validateInput(Integer month, Integer year, Map<String, Integer> values) {
        if (month == null || month < 1 || month > 12) {
            throw new NotValidArgumentException("Month must be between 1 and 12.");
        }

        if (year == null || year < 0) {
            throw new NotValidArgumentException("Year must be a positive number.");
        }

        for (Integer value : values.values()) {
            if (value == null || value < 0) {
                throw new NotValidArgumentException("Readings must be positive numbers.");
            }
        }
        List<String> validTypes = readingStructureService.getReadingTypes();
        for (String key : values.keySet()) {
            if (!validTypes.contains(key)) {
                throw new NotValidArgumentException("Invalid reading type: " + key);
            }
        }
    }
}

