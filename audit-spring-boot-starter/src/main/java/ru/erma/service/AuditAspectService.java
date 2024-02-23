package ru.erma.service;

import ru.erma.model.Audit;

public interface AuditAspectService {
    void saveAudit(Audit audit);
}