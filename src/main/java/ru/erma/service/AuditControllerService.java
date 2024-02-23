package ru.erma.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.erma.dto.AuditDTO;
import ru.erma.exception.NoLogsFoundException;
import ru.erma.mappers.AuditMapper;
import ru.erma.model.Audit;
import ru.erma.repository.AuditRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditControllerService {
    private final AuditRepository<Audit> auditRepository;
    private final AuditMapper mapper;

    @ru.erma.aop.annotations.Audit(action = "Admin viewed all audits")
    public List<AuditDTO> getAllAudits(){
        List<Audit> audits = auditRepository.findAll();
        if (audits.isEmpty()) {
            throw new NoLogsFoundException("No audit logs found");
        }
        return mapper.toAuditListDTO(audits);
    }
}
