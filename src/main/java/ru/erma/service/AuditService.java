package ru.erma.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.erma.dto.AuditDTO;
import ru.erma.exception.NoLogsFoundException;
import ru.erma.mappers.AuditMapper;
import ru.erma.model.Audit;
import ru.erma.repository.AuditRepository;

import java.util.List;

/**
 * This class provides services related to Audit operations for the controller.
 * It uses an AuditRepository to perform operations on Audit data and an AuditMapper to convert between Audit and AuditDTO objects.
 */
@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditRepository auditRepository;

    private final AuditMapper mapper;

    /**
     * This method is used to retrieve all audit records from the database.
     * It uses the AuditRepository's findAll method to do this.
     *
     * @return a list of AuditDTO objects representing all audits in the database
     * @throws NoLogsFoundException if no audits are found in the database
     */
    @ru.erma.aop.annotations.Audit(action = "Admin viewed all audits")
    public List<AuditDTO> getAllAudits(){
        List<Audit> audits = auditRepository.findAll();
        if (audits.isEmpty()) {
            throw new NoLogsFoundException("No audit logs found");
        }
        return mapper.toDtoList(audits);
    }
}
