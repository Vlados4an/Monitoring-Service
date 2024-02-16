package ru.erma.mappers;

import org.mapstruct.Mapper;
import ru.erma.dto.AuditListDTO;
import ru.erma.model.Audit;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuditMapper {

    default AuditListDTO toAuditListDTO(List<Audit> audits) {
        AuditListDTO auditListDTO = new AuditListDTO();
        auditListDTO.setAudits(audits);
        return auditListDTO;
    }
}