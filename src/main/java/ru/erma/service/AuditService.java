package ru.erma.service;

import lombok.RequiredArgsConstructor;
import ru.erma.model.Audit;
import ru.erma.repository.AuditRepository;

import java.util.List;

/**
 * This class provides services related to AuditLog operations.
 * It uses an AuditRepository to perform operations on AuditLog data.
 */
@RequiredArgsConstructor
public class AuditService {

    private final AuditRepository<Audit> auditRepository;

    /**
     * Logs an action by creating a new AuditLog and saving it in the AuditRepository.
     *
     * @param action the action to log
     */
    public void logAction(String action){
        Audit audit = new Audit();
        audit.getAudits().add(action);
        auditRepository.save(audit);
    }

    /**
     * Retrieves all AuditLog objects from the AuditRepository.
     *
     * @return a list of all AuditLog objects
     */
    public List<Audit> getAllAudits(){
        return auditRepository.findAll();
    }
}
