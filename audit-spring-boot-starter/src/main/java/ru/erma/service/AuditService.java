package ru.erma.service;

import ru.erma.model.Audit;

public interface AuditService {
    void saveAudit(Audit audit);
}