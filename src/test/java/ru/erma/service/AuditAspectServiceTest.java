package ru.erma.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.erma.model.Audit;
import ru.erma.repository.AuditRepository;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

/**
 * This class is used to test the AuditService class.
 * It uses the Mockito framework for mocking objects and JUnit for running the tests.
 */
@ExtendWith(MockitoExtension.class)
class AuditAspectServiceTest {

    /**
     * Mock of AuditRepository used in the tests.
     */
    @Mock
    private AuditRepository auditRepository;

    /**
     * The AuditService instance under test, with mocked dependencies.
     */
    @InjectMocks
    private AuditAspectServiceImpl auditService;

    /**
     * Tests that the saveAudit method correctly saves an audit.
     * It creates an audit, calls the saveAudit method with the audit, and verifies that the save method of the AuditRepository was called with the audit.
     * Asserts that no exception is thrown when saving the audit.
     */
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
}