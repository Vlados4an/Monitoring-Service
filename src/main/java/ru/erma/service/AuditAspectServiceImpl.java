package ru.erma.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.erma.dto.AuditDTO;
import ru.erma.mappers.AuditMapper;
import ru.erma.repository.AuditRepository;

/**
 * This class provides services related to Audit operations.
 * It uses an AuditRepository to perform operations on Audit data.
 */
@Service
@RequiredArgsConstructor
public class AuditAspectServiceImpl implements AuditAspectService {

    private final AuditRepository auditRepository;

    private final AuditMapper auditMapper;

    /**
     * Saves the given audit record to the AuditRepository.
     *
     * @param auditDTO the audit record to save
     */
    @Override
    public void saveAudit(AuditDTO auditDTO) {
        auditRepository.save(auditMapper.toEntity(auditDTO));
    }

}
