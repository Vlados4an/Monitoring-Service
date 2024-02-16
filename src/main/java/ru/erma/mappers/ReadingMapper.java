package ru.erma.mappers;

import org.mapstruct.Mapper;
import ru.erma.dto.ReadingDTO;
import ru.erma.dto.ReadingListDTO;
import ru.erma.dto.ReadingRequest;
import ru.erma.model.Reading;

import java.util.List;

/**
 * The ReadingMapper interface is used to map between ReadingRequest DTOs and Reading entities.
 * It uses the MapStruct library to generate the implementation of the mapping methods.
 * It contains a method to map from a ReadingRequest to a Reading.
 */
@Mapper(componentModel = "spring")
public interface ReadingMapper {

    Reading toReading(ReadingRequest readingRequest);
    ReadingDTO toReadingDTO(Reading reading);

    default ReadingListDTO toReadingListDTO(List<Reading> readings) {
        ReadingListDTO readingListDTO = new ReadingListDTO();
        readingListDTO.setReadings(readings);
        return readingListDTO;
    }

}