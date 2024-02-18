package ru.erma.repository;

import ru.erma.model.Reading;

import java.util.List;
import java.util.Optional;

/**
 * This interface represents a repository for readings.
 * It provides methods to save a reading and to retrieve readings based on username, month, and year.
 *
 * @param <K> the type of the username
 * @param <E> the type of the reading
 */
public interface ReadingRepository<K,E> {

    /**
     * Saves the given reading for the specified username.
     *
     * @param username the username for which to save the reading
     * @param reading the reading to save
     */
    void save(K username, E reading);

    /**
     * Retrieves all readings for the specified username.
     *
     * @param username the username for which to retrieve the readings
     * @return a list of readings for the specified username
     */
    List<E> findByUsername(K username);

    /**
     * Retrieves all readings for the specified username, month, and year.
     *
     * @param username the username for which to retrieve the readings
     * @param month the month for which to retrieve the readings
     * @param year the year for which to retrieve the readings
     * @return a list of readings for the specified username, month, and year
     */
    List<E> findByUsernameAndMonthAndYear(K username, int month, int year);

    /**
     * Retrieves the latest reading for the specified username.
     *
     * @param username the username for which to retrieve the latest reading
     * @return an Optional containing the latest reading for the specified username, or an empty Optional if no readings are found
     */
    Optional<Reading> findLatestByUsername(K username);
}