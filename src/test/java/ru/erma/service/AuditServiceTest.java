package ru.erma.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.erma.model.AuditLog;
import ru.erma.repository.AuditRepository;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * This class is used to test the AuditService class.
 * It uses the Mockito framework for mocking objects and JUnit for running the tests.
 */
@ExtendWith(MockitoExtension.class)
class AuditServiceTest {

    /**
     * Mock of AuditRepository used in the tests.
     */
    @Mock
    private AuditRepository<AuditLog> auditRepository;

    /**
     * The AuditService instance under test, with mocked dependencies.
     */
    @InjectMocks
    private AuditService auditService;

    /**
     * This test verifies that when the logAction method is called,
     * it saves a new AuditLog with the provided action.
     */
    @Test
    void logAction_savesNewAuditLogWithAction() {
        String action = "testAction";
        auditService.logAction(action);
        verify(auditRepository,times(1)).save(any(AuditLog.class));
    }

    /**
     * This test verifies that when the getAllAudits method is called,
     * it returns all AuditLogs from the repository.
     */
    @Test
    void getAllAudits_returnsAllAuditLogsFromRepository(){
        AuditLog log1 = new AuditLog();
        log1.getLogs().add("action1");
        AuditLog log2 = new AuditLog();
        log2.getLogs().add("action2");
        when(auditRepository.findAll()).thenReturn(Arrays.asList(log1,log2));

        List<AuditLog> allAudits = auditService.getAllAudits();

        assertThat(allAudits).hasSize(2);
        assertThat(allAudits.get(0).getLogs().get(0)).isEqualTo("action1");
        assertThat(allAudits.get(1).getLogs().get(0)).isEqualTo("action2");
    }
}