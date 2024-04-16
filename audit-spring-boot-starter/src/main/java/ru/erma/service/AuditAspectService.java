package ru.erma.service;

import ru.erma.dto.AuditDTO;

public interface AuditAspectService {
    void saveAudit(AuditDTO audit);
}