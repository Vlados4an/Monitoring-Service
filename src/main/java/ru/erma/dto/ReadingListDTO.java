package ru.erma.dto;

import lombok.Getter;
import lombok.Setter;
import ru.erma.model.Reading;

import java.util.List;

@Getter
@Setter
public class ReadingListDTO {
    private List<Reading> readings;
}