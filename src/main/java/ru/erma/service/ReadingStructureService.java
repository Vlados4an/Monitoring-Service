package ru.erma.service;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides services related to reading types.
 * It maintains a list of reading types and provides methods to add and remove reading types.
 */
@Getter
public class ReadingStructureService {

    // List of reading types
    private final List<String> readingTypes;

    /**
     * Constructor for the ReadingStructureService class.
     * Initializes the list of reading types with some default types.
     */
    public ReadingStructureService(){
        this.readingTypes = new ArrayList<>();
        this.readingTypes.add("heating");
        this.readingTypes.add("cold water");
        this.readingTypes.add("hot water");
    }

    /**
     * Adds a new reading type to the list.
     *
     * @param type the reading type to add
     */
    public void addReadingType(String type){
        readingTypes.add(type);
    }

    /**
     * Removes a reading type from the list.
     * If the reading type does not exist, prints a message and returns false.
     *
     * @param type the reading type to remove
     * @return true if the reading type was removed, false otherwise
     */
    public boolean removeReadingType(String type) {
        if (!readingTypes.contains(type)) {
            System.out.println("Такого типа показаний не существует.");
            return false;
        } else {
            readingTypes.remove(type);
            return true;
        }
    }
}