package ru.erma.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.erma.model.AuditLog;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class contains unit tests for the AuditRepositoryImpl class.
 */
public class AuditRepositoryImplTest {
    private AuditRepositoryImpl auditRepository;

    /**
     * This method is executed before each test.
     * It initializes the auditRepository instance.
     */
    @BeforeEach
    void setUp(){
        auditRepository = new AuditRepositoryImpl();
    }

    /**
     * This test verifies that the save method of AuditRepositoryImpl adds an audit log to the repository.
     */
    @Test
    void save_addsAuditLogToRepository() {
        AuditLog auditLog = new AuditLog();
        auditRepository.save(auditLog);

        List<AuditLog> allAuditLogs = auditRepository.findAll();
        assertThat(allAuditLogs).hasSize(1).contains(auditLog);
    }

    /**
     * This test verifies that the save method of AuditRepositoryImpl assigns an ID to the audit log.
     */
    @Test
    void save_assignsIdToAuditLog() {
        AuditLog auditLog = new AuditLog();
        auditRepository.save(auditLog);

        assertThat(auditLog.getId()).isEqualTo(1L);
    }

    /**
     * This test verifies that the findAll method of AuditRepositoryImpl returns all audit logs in the repository.
     */
    @Test
    void findAll_returnsAllAuditLogs() {
        AuditLog auditLog1 = new AuditLog();
        AuditLog auditLog2 = new AuditLog();
        auditRepository.save(auditLog1);
        auditRepository.save(auditLog2);

        List<AuditLog> allAuditLogs = auditRepository.findAll();

        assertThat(allAuditLogs).hasSize(2).contains(auditLog1, auditLog2);
    }

    /**
     * This test verifies that the findAll method of AuditRepositoryImpl returns an empty list when there are no audit logs in the repository.
     */
    @Test
    void findAll_returnsEmptyListWhenNoAuditLogs() {
        List<AuditLog> allAuditLogs = auditRepository.findAll();

        assertThat(allAuditLogs).isEmpty();
    }
}