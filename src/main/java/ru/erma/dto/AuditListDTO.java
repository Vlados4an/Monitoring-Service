package ru.erma.dto;

import lombok.Getter;
import lombok.Setter;
import ru.erma.model.Audit;

import java.util.List;
@Getter
@Setter
public class AuditListDTO {
    private List<Audit> audits;
}
