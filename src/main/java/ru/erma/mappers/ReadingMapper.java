package ru.erma.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.erma.dto.ReadingRequest;
import ru.erma.model.Reading;

@Mapper
public interface ReadingMapper {
    ReadingMapper INSTANCE = Mappers.getMapper(ReadingMapper.class);
    Reading toReading(ReadingRequest readingRequest);
}