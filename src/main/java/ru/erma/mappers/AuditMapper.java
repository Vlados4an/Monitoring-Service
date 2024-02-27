package ru.erma.mappers;

import org.mapstruct.Mapper;
import ru.erma.dto.AuditDTO;
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
     * This method is used to convert a list of Audit objects into a list of AuditDTO objects.
     *
     * @param audits the list of Audit objects to convert
     * @return a list of AuditDTO objects
     */
    List<AuditDTO> toAuditListDTO(List<Audit> audits);
}