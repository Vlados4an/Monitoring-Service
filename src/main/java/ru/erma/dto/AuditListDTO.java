package ru.erma.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ru.erma.model.Audit;

import java.util.List;

@Getter
@Setter
@Schema(description = "Data object representing a list of audits")
public class AuditListDTO {
    @Schema(description = "List of audits")
    private List<Audit> audits;
}