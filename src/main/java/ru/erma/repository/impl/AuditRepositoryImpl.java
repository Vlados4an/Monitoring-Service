package ru.erma.repository.impl;

import ru.erma.model.AuditLog;
import ru.erma.repository.AuditRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides an implementation of the AuditRepository interface.
 * It uses a HashMap to store AuditLog objects, using their ID as the key.
 */
public class AuditRepositoryImpl implements AuditRepository<AuditLog> {
    Map<Long, AuditLog> auditLogs = new HashMap<>();
    private Long idCounter = 1L;

    /**
     * Saves the given AuditLog object.
     * The AuditLog is stored in the auditLogs map, using a unique ID as the key.
     *
     * @param auditLog the AuditLog object to save
     */
    @Override
    public void save(AuditLog auditLog) {
        auditLog.setId(idCounter);
        auditLogs.put(idCounter++, auditLog);
    }

    /**
     * Retrieves all AuditLog objects.
     *
     * @return a list of all AuditLog objects
     */
    @Override
    public List<AuditLog> findAll() {
        return new ArrayList<>(auditLogs.values());
    }
}