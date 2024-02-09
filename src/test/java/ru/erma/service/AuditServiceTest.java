package ru.erma.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.erma.exception.NotValidArgumentException;
import ru.erma.model.Audit;
import ru.erma.repository.AuditRepository;
import ru.erma.service.AuditService;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    private AuditRepository<Audit> auditRepository;

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
    @DisplayName("LogAction method saves a new Audit with the provided action")
    void logAction_savesNewAuditLogWithAction() {
        String action = "testAction";
        auditService.logAction(action);
        verify(auditRepository,times(1)).save(any(Audit.class));
    }

    /**
     * This test verifies that when the logAction method saves null,
     * it throws NotValidArgumentException.
     */
    @Test
    @DisplayName("Test that exception is thrown when saving null audit")
    void logNullAction_throwsNotValidArgumentException() {
        assertThatThrownBy(() -> auditService.logAction(null))
                .isInstanceOf(NotValidArgumentException.class);
    }

    /**
     * This test verifies that when the getAllAudits method is called,
     * it returns all AuditLogs from the repository.
     */
    @Test
    @DisplayName("GetAllAudits method returns all Audits from the repository")
    void getAllAudits_returnsAllAuditLogsFromRepository(){
        Audit log1 = new Audit();
        log1.getAudits().add("action1");
        Audit log2 = new Audit();
        log2.getAudits().add("action2");
        when(auditRepository.findAll()).thenReturn(Arrays.asList(log1,log2));

        List<Audit> allAudits = auditService.getAllAudits();

        assertThat(allAudits).hasSize(2);
        assertThat(allAudits.get(0).getAudits().get(0)).isEqualTo("action1");
        assertThat(allAudits.get(1).getAudits().get(0)).isEqualTo("action2");
    }
}