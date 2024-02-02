package ru.erma.service;

import lombok.Getter;
import ru.erma.repository.ReadingTypeRepository;


import java.util.ArrayList;
import java.util.List;
@Getter
public class ReadingStructureService {

    private final ReadingTypeRepository<String> readingTypeRepository;
    private List<String> readingTypes = new ArrayList<>();

    public ReadingStructureService(ReadingTypeRepository<String> readingTypeRepository) {
        this.readingTypeRepository = readingTypeRepository;
        updateReadingTypes();
    }

    public void addReadingType(String type) {
        readingTypes.add(type);
        readingTypeRepository.addColumnToReadingsTable(type);
    }

    public boolean removeReadingType(String type) {
        boolean removed = readingTypes.remove(type);
        if (removed) {
            readingTypeRepository.removeColumnFromReadingsTable(type);
        }
        return removed;
    }
    private void updateReadingTypes() {
        readingTypes = readingTypeRepository.getReadingTypesFromDatabase();
    }
}