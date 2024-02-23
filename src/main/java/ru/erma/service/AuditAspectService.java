package ru.erma.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.erma.model.Audit;
import ru.erma.repository.AuditRepository;


/**
 * This class provides services related to Audit operations.
 * It uses an AuditRepository to perform operations on Audit data.
 */
@Service
@RequiredArgsConstructor
public class AuditAspectService implements AuditService {

    private final AuditRepository<Audit> auditRepository;

    /**
     * Saves the given audit record to the AuditRepository.
     *
     * @param audit the audit record to save
     */
    @Override
    public void saveAudit(Audit audit) {
        auditRepository.save(audit);
    }

}
