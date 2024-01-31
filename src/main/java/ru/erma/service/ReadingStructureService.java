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

    public void removeReadingType(String type) {
        readingTypes.remove(type);
        readingTypeRepository.removeColumnFromReadingsTable(type);
    }
    private void updateReadingTypes() {
        readingTypes = readingTypeRepository.getReadingTypesFromDatabase();
    }
}