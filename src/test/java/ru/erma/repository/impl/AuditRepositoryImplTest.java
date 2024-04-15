package ru.erma.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.erma.config.AbstractTestContainerConfig;
import ru.erma.model.Audit;
import ru.erma.repository.AuditRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * This class is responsible for testing the AuditRepositoryImpl class.
 * It extends AbstractTestContainerConfig to use a PostgreSQL test container.
 */
public class AuditRepositoryImplTest extends AbstractTestContainerConfig {

    @Autowired
    private AuditRepository auditRepository;

    /**
     * Tests that the save method correctly saves an audit to the database.
     * It saves an audit to the database and asserts that no exception is thrown.
     */
    @Test
    @DisplayName("Audit is saved correctly")
    void shouldSaveAudit() {
        Audit audit = new Audit();
        audit.setAction("testAction");
        audit.setUsername("petya");
        audit.setTimestamp(LocalDateTime.now());

        assertThatCode(() -> auditRepository.save(audit)).doesNotThrowAnyException();
    }

    /**
     * Tests that the save method throws an exception when trying to save a null audit.
     * Asserts that a RuntimeException is thrown when trying to save a null audit.
     */
    @Test
    @DisplayName("Exception is thrown when saving null audit")
    void shouldThrowExceptionWhenSavingNullAudit() {
        assertThatThrownBy(() -> auditRepository.save(null))
                .isInstanceOf(RuntimeException.class);
    }

    /**
     * Tests that the findAll method correctly retrieves all audits from the database.
     * It saves an audit to the database, retrieves all audits, and asserts that the list is not empty and that the first audit's action matches the expected action.
     */
    @Test
    @DisplayName("All audits are retrieved correctly")
    void shouldFindAllAudits() {
        Audit audit = new Audit();
        audit.setAction("testAction");
        audit.setUsername("petya");
        audit.setTimestamp(LocalDateTime.now());

        auditRepository.save(audit);

        List<Audit> audits = auditRepository.findAll();

        assertThat(audits).isNotEmpty();
        assertThat(audits.get(0).getAction()).isEqualTo("testAction");
    }

    /**
     * Tests that the findAll method correctly handles the case where there are no audits in the database.
     * It retrieves all audits and asserts that the returned list is empty.
     */
    @Test
    @DisplayName("Empty list is returned when there are no audits")
    void shouldReturnEmptyListWhenNoAudits() {
        List<Audit> audits = auditRepository.findAll();

        assertThat(audits).isEmpty();
    }
}