package ru.erma.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.erma.dto.AuditListDTO;
import ru.erma.exception.NoLogsFoundException;
import ru.erma.mappers.AuditMapper;
import ru.erma.model.Audit;
import ru.erma.repository.AuditRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * This class is used to test the AuditService class.
 * It uses the Mockito framework for mocking objects and JUnit for running the tests.
 */
@ExtendWith(MockitoExtension.class)
class AuditServiceTest {
    @Mock
    private AuditMapper auditMapper;

    /**
     * Mock of AuditRepository used in the tests.
     */
    @Mock
    private AuditRepository<Audit> auditRepository;

    /**
     * The AuditService instance under test, with mocked dependencies.
     */
    @InjectMocks
    private AuditService auditService;
    @Test
    @DisplayName("SaveAudit method saves the audit successfully")
    void saveAudit_savesAuditSuccessfully() {
        Audit audit = new Audit();
        audit.setAction("action");
        audit.setUsername("test");
        audit.setTimestamp(LocalDateTime.now());

        auditService.saveAudit(audit);

        verify(auditRepository, times(1)).save(audit);
    }

    /**
     * This test verifies that when the getAllAudits method is called,
     * it returns all AuditLogs from the repository.
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

        AuditListDTO auditListDTO = new AuditListDTO();
        auditListDTO.setAudits(audits);
        when(auditMapper.toAuditListDTO(audits)).thenReturn(auditListDTO);

        AuditListDTO allAudits = auditService.getAllAudits();

        assertThat(allAudits).isNotNull();
        assertThat(allAudits.getAudits()).hasSize(2);
        assertThat(allAudits.getAudits().get(0)).isEqualTo(audit1);
        assertThat(allAudits.getAudits().get(1)).isEqualTo(audit2);
    }

    @Test
    @DisplayName("GetAllAudits method returns empty list when no audits are found")
    void getAllAudits_throwsNoLogsFoundException() {
        when(auditRepository.findAll()).thenReturn(Collections.emptyList());

        assertThatThrownBy(()->auditService.getAllAudits())
                .isInstanceOf(NoLogsFoundException.class)
                .hasMessage("No audit logs found");
    }
}