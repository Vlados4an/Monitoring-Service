package ru.erma.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.erma.dto.AuditListDTO;
import ru.erma.exception.NoLogsFoundException;
import ru.erma.mappers.AuditMapper;
import ru.erma.model.Audit;
import ru.erma.repository.AuditRepository;

import java.util.List;

/**
 * This class provides services related to Audit operations.
 * It uses an AuditRepository to perform operations on Audit data.
 */
@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditRepository<Audit> auditRepository;
    private final AuditMapper mapper;

    /**
     * Saves the given audit record to the AuditRepository.
     *
     * @param audit the audit record to save
     */
    public void saveAudit(Audit audit) {
        auditRepository.save(audit);
    }

    /**
     * Retrieves all Audit objects from the AuditRepository.
     *
     * @return a list of all Audit objects
     */
    @ru.erma.aop.annotations.Audit(action = "Admin viewed all audits")
    public AuditListDTO getAllAudits(){
        List<Audit> audits = auditRepository.findAll();
        if (audits.isEmpty()) {
            throw new NoLogsFoundException("No audit logs found");
        }
        return mapper.toAuditListDTO(audits);
    }
}
