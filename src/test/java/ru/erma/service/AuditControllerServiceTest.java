package ru.erma.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.erma.dto.AuditDTO;
import ru.erma.exception.NoLogsFoundException;
import ru.erma.mappers.AuditMapper;
import ru.erma.model.Audit;
import ru.erma.repository.AuditRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class AuditControllerServiceTest {
    @Mock
    private AuditRepository<Audit> auditRepository;
    @Mock
    private AuditMapper auditMapper;
    @InjectMocks
    private AuditControllerService auditService;
    /**
     * Tests that the getAllAudits method correctly retrieves all audits.
     * It creates two audits, saves them to the AuditRepository, and calls the getAllAudits method.
     * Asserts that the returned AuditListDTO is not null, that it contains two audits, and that the audits match the expected audits.
     */
    @Test
    @DisplayName("GetAllAudits method returns all Audits from the repository")
    void getAllAudits_returnsAllAuditLogsFromRepository() {
        Audit audit1 = new Audit();
        audit1.setAction("action1");
        audit1.setUsername("test");
        audit1.setTimestamp(LocalDateTime.now());
        Audit audit2 = new Audit();
        audit2.setAction("action2");
        audit1.setUsername("test");
        audit1.setTimestamp(LocalDateTime.now());

        List<Audit> audits = List.of(audit1,audit2);

        when(auditRepository.findAll()).thenReturn(audits);

        AuditDTO auditDTO1 = new AuditDTO(audit1.getUsername(), audit1.getTimestamp(), audit1.getAction());
        AuditDTO auditDTO2 = new AuditDTO(audit2.getUsername(), audit2.getTimestamp(), audit2.getAction());
        List<AuditDTO> auditDTOs = List.of(auditDTO1, auditDTO2);

        when(auditMapper.toAuditListDTO(audits)).thenReturn(auditDTOs);

        List<AuditDTO> allAudits = auditService.getAllAudits();

        assertThat(allAudits).isNotNull();
        assertThat(allAudits.size()).isEqualTo(2);
        assertThat(allAudits.get(0)).isEqualTo(auditDTO1);
        assertThat(allAudits.get(1)).isEqualTo(auditDTO2);
    }

    /**
     * Tests that the getAllAudits method correctly handles the case where there are no audits.
     * It calls the getAllAudits method and asserts that a NoLogsFoundException is thrown with the message "No audit logs found".
     */
    @Test
    @DisplayName("GetAllAudits method returns empty list when no audits are found")
    void getAllAudits_throwsNoLogsFoundException() {
        when(auditRepository.findAll()).thenReturn(Collections.emptyList());

        assertThatThrownBy(()->auditService.getAllAudits())
                .isInstanceOf(NoLogsFoundException.class)
                .hasMessage("No audit logs found");
    }
}
