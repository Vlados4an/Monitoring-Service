package ru.erma.service;

import ru.erma.model.AuditDTO;

public interface AuditAspectService {
    void saveAudit(AuditDTO audit);
}