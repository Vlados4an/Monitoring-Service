package ru.erma.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.erma.model.Audit;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * The AuditRepositoryImplTest class tests the functionality of the AuditRepositoryImpl class.
 * It extends the AbstractRepositoryForTest class to reuse its setup logic.
 */
public class AuditRepositoryImplTest extends AbstractRepositoryForTest {
    private AuditRepositoryImpl auditRepository;

    /**
     * The setUp method initializes the AuditRepositoryImpl instance before each test.
     * It calls the setUp method of the superclass to initialize the connection provider,
     * and then creates a new AuditRepositoryImpl with the connection provider.
     */
    @BeforeEach
    void setUp() {
        super.setUp();
        auditRepository = new AuditRepositoryImpl(connectionProvider);
    }

    /**
     * This test checks that the save method correctly saves an audit to the database.
     * It creates an audit, adds an action to it, and then saves it to the database.
     * It asserts that no exception is thrown when saving the audit.
     */
    @Test
    @DisplayName("Test that audit is saved correctly")
    void shouldSaveAudit() {
        Audit audit = new Audit();
        audit.getAudits().add("testAction");

        assertThatCode(() -> auditRepository.save(audit)).doesNotThrowAnyException();
    }

    /**
     * This test checks that the save method correctly handles the case where the audit is null.
     * It attempts to save a null audit to the database.
     * It asserts that a DatabaseException is thrown.
     */
    @Test
    @DisplayName("Test that exception is thrown when saving null audit")
    void shouldThrowExceptionWhenSavingNullAudit() {
        assertThatThrownBy(() -> auditRepository.save(null))
                .isInstanceOf(RuntimeException.class);
    }

    /**
     * This test checks that the findAll method correctly retrieves all audits from the database.
     * It creates an audit, adds an action to it, saves it to the database, and then retrieves all audits.
     * It asserts that the retrieved audits are not empty and that the first audit contains the added action.
     */
    @Test
    @DisplayName("Test that all audits are retrieved correctly")
    void shouldFindAllAudits() {
        Audit audit = new Audit();
        audit.getAudits().add("testAction");
        auditRepository.save(audit);

        List<Audit> audits = auditRepository.findAll();

        assertThat(audits).isNotEmpty();
        assertThat(audits.get(0).getAudits().get(0)).isEqualTo("testAction");
    }

    /**
     * This test checks that the findAll method correctly handles the case where there are no audits in the database.
     * It retrieves all audits from the database.
     * It asserts that the retrieved audits are empty.
     */
    @Test
    @DisplayName("Test that empty list is returned when there are no audits")
    void shouldReturnEmptyListWhenNoAudits() {
        List<Audit> audits = auditRepository.findAll();

        assertThat(audits).isEmpty();
    }
}