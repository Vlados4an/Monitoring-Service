package ru.erma.mappers;

import org.mapstruct.Mapper;
import ru.erma.dto.AuditListDTO;
import ru.erma.model.Audit;

import java.util.List;

/**
 * This interface is used to map between Audit objects and AuditListDTO objects.
 * It uses the MapStruct framework to generate the implementation code.
 * The @Mapper annotation specifies that the interface is a MapStruct mapper and the component model is "spring".
 */
@Mapper(componentModel = "spring")
public interface AuditMapper {

    /**
     * This method converts a list of Audit objects to an AuditListDTO object.
     * The AuditListDTO object contains the list of Audit objects.
     *
     * @param audits the list of Audit objects
     * @return the AuditListDTO object
     */
    default AuditListDTO toAuditListDTO(List<Audit> audits) {
        AuditListDTO auditListDTO = new AuditListDTO();
        auditListDTO.setAudits(audits);
        return auditListDTO;
    }
}