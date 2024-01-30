package ru.erma.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.erma.model.Audit;

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
    @DisplayName("Save method adds an audit to the repository")
    void save_addsAuditLogToRepository() {
        Audit audit = new Audit();
        auditRepository.save(audit);

        List<Audit> allAudits = auditRepository.findAll();
        assertThat(allAudits).hasSize(1).contains(audit);
    }

    /**
     * This test verifies that the save method of AuditRepositoryImpl assigns an ID to the audit log.
     */
    @Test
    @DisplayName("Save method assigns an ID to the audit")
    void save_assignsIdToAuditLog() {
        Audit audit = new Audit();
        auditRepository.save(audit);

        assertThat(audit.getId()).isEqualTo(1L);
    }

    /**
     * This test verifies that the findAll method of AuditRepositoryImpl returns all audit logs in the repository.
     */
    @Test
    @DisplayName("FindAll method returns all audits in the repository")
    void findAll_returnsAllAuditLogs() {
        Audit audit1 = new Audit();
        Audit audit2 = new Audit();
        auditRepository.save(audit1);
        auditRepository.save(audit2);

        List<Audit> allAudits = auditRepository.findAll();

        assertThat(allAudits).hasSize(2).contains(audit1, audit2);
    }

    /**
     * This test verifies that the findAll method of AuditRepositoryImpl returns an empty list when there are no audit logs in the repository.
     */
    @Test
    @DisplayName("FindAll method returns an empty list when there are no audit logs in the repository")
    void findAll_returnsEmptyListWhenNoAuditLogs() {
        List<Audit> allAudits = auditRepository.findAll();

        assertThat(allAudits).isEmpty();
    }
}