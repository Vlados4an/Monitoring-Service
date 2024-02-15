package ru.erma.service;

import ru.erma.aop.annotations.Audit;
import ru.erma.exception.NotValidArgumentException;
import ru.erma.repository.ReadingTypeRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * The ReadingStructureService class provides methods to manage the structure of the readings in the database.
 * It uses a ReadingTypeRepository to add and remove reading types, and to get the reading types from the database.
 * It maintains a list of the current reading types.
 */
public class ReadingStructureService {

    private final ReadingTypeRepository<String> readingTypeRepository;

    private List<String> readingTypes = new ArrayList<>();

    /**
     * Constructs a new ReadingStructureService with the specified reading type repository.
     * It updates the list of reading types from the database.
     *
     * @param readingTypeRepository the repository for reading types.
     */
    public ReadingStructureService(ReadingTypeRepository<String> readingTypeRepository) {
        this.readingTypeRepository = readingTypeRepository;
        updateReadingTypes();
    }

    /**
     * Adds a reading type to the list and the database.
     * It adds the reading type to the list and calls the repository's method to add a column to the readings table.
     *
     * @param type the reading type to add.
     */
    @Audit(action = "Admin added new reading type: ")
    public void addReadingType(String type) {
        if (readingTypes.contains(type)) {
            throw new NotValidArgumentException("Reading type " + type + " already exists.");
        }
        readingTypes.add(type);
        readingTypeRepository.addColumnToReadingsTable(type);
    }

    /**
     * Removes a reading type from the list and the database.
     * It removes the reading type from the list and, if successful, calls the repository's method to remove a column from the readings table.
     *
     * @param type the reading type to remove.
     * @return true if the reading type was removed from the list, false otherwise.
     */
    @Audit(action = "Admin removed reading type: ")
    public boolean removeReadingType(String type) {
        boolean removed = readingTypes.remove(type);
        if (removed) {
            readingTypeRepository.removeColumnFromReadingsTable(type);
        }
        return removed;
    }

    /**
     * Updates the list of reading types from the database.
     * It calls the repository's method to get the reading types from the database and sets the list to the returned value.
     */
    private void updateReadingTypes() {
        readingTypes = readingTypeRepository.getReadingTypesFromDatabase();
    }


    public List<String> getReadingTypes() {
        return readingTypes;
    }
}