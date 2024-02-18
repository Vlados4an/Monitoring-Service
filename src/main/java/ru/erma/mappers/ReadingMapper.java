package ru.erma.mappers;

import org.mapstruct.Mapper;
import ru.erma.dto.ReadingDTO;
import ru.erma.dto.ReadingListDTO;
import ru.erma.dto.ReadingRequest;
import ru.erma.model.Reading;

import java.util.List;

/**
 * This interface is used to map between ReadingRequest, ReadingDTO, ReadingListDTO objects and Reading entities.
 * It uses the MapStruct framework to generate the implementation code.
 * The @Mapper annotation specifies that the interface is a MapStruct mapper and the component model is "spring".
 */
@Mapper(componentModel = "spring")
public interface ReadingMapper {

    /**
     * This method converts a ReadingRequest object to a Reading entity.
     * @param readingRequest the ReadingRequest object
     * @return the Reading entity
     */
    Reading toReading(ReadingRequest readingRequest);

    /**
     * This method converts a Reading entity to a ReadingDTO object.
     * @param reading the Reading entity
     * @return the ReadingDTO object
     */
    ReadingDTO toReadingDTO(Reading reading);

    /**
     * This method converts a list of Reading entities to a ReadingListDTO object.
     * The ReadingListDTO object contains the list of Reading entities.
     *
     * @param readings the list of Reading entities
     * @return the ReadingListDTO object
     */
    default ReadingListDTO toReadingListDTO(List<Reading> readings) {
        ReadingListDTO readingListDTO = new ReadingListDTO();
        readingListDTO.setReadings(readings);
        return readingListDTO;
    }

}