package ru.erma.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.erma.aop.annotations.Audit;
import ru.erma.dto.ReadingDTO;
import ru.erma.dto.ReadingListDTO;
import ru.erma.dto.ReadingRequest;
import ru.erma.exception.ReadingAlreadyExistsException;
import ru.erma.exception.ReadingNotFoundException;
import ru.erma.mappers.ReadingMapper;
import ru.erma.model.Reading;
import ru.erma.repository.ReadingRepository;

import java.util.List;


/**
 * The ReadingService class provides services related to Reading operations.
 * It uses a ReadingRepository to perform operations on Reading data.
 * It also uses a ReadingStructureService to manage the structure of readings.
 */
@Service
@RequiredArgsConstructor
public class ReadingService {

    private final ReadingRepository<String, Reading> readingRepository;

    private final ReadingMapper readingMapper;

    /**
     * Submits readings for a user.
     * It validates the input, checks if readings for the specified month and year already exist, and saves the new readings.
     * If readings for the specified month and year already exist, it throws a ReadingAlreadyExistsException.
     * If the input is not valid, it throws a NotValidArgumentException.
     *
     * @param request the ReadingRequest containing the readings to be submitted
     */
    @Audit(action = "User submitted readings")
    public void submitReadings(ReadingRequest request) {
        List<Reading> existingReading = readingRepository.findByUsernameAndMonthAndYear(request.username(),request.month(),request.year());

        if (existingReading != null && !existingReading.isEmpty()) {
            throw new ReadingAlreadyExistsException("Reading for the specified month and year already exists!");
        }

        Reading newReading = readingMapper.toReading(request);
        readingRepository.save(request.username(), newReading);
    }

    /**
     * Retrieves all readings for a user for a specific month and year.
     *
     * @param username the username of the user
     * @param month    the month of the readings
     * @param year     the year of the readings
     * @return a list of readings for the specified username, month, and year
     */
    @Audit(action = "User viewed readings for month")
    public ReadingListDTO getReadingsForMonth(String username, Integer month, Integer year){
        List<Reading> userReadings = readingRepository.findByUsernameAndMonthAndYear(username, month, year);
        if (userReadings == null || userReadings.isEmpty()) {
            throw new ReadingNotFoundException("No readings found for user with username " + username + " for month " + month + " and year " + year);
        }
        return readingMapper.toReadingListDTO(userReadings);
    }

    /**
     * Retrieves the reading history for a user.
     *
     * @param username the username of the user
     * @return a list of all readings for the specified username
     */
    @Audit(action = "User viewed reading history")
    public ReadingListDTO getReadingHistory(String username) {
        List<Reading> readings = getUserReadings(username);
        return readingMapper.toReadingListDTO(readings);
    }


    /**
     * Retrieves the most recent readings for a user.
     *
     * @param username the username of the user
     * @return the most recent Reading object for the specified username, or null if no readings exist
     */
    @Audit(action = "User viewed actual readings")
    public ReadingDTO getActualReadings(String username) {
        Reading reading = readingRepository.findLatestByUsername(username)
                .orElseThrow(()->new ReadingNotFoundException("No readings found for user with username " + username));
        return readingMapper.toReadingDTO(reading);
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

}

