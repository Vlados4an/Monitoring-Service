package ru.erma.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.erma.model.Audit;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class AuditRepositoryImplTest extends AbstractRepositoryForTest {

    private AuditRepositoryImpl auditRepository;

    @BeforeEach
    void setUp() {
        super.setUp();
        auditRepository = new AuditRepositoryImpl(jdbcTemplate);
    }

    @Test
    @DisplayName("Audit is saved correctly")
    void shouldSaveAudit() {
        Audit audit = new Audit();
        audit.setAction("testAction");
        audit.setUsername("petya");
        audit.setTimestamp(LocalDateTime.now());

        assertThatCode(() -> auditRepository.save(audit)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Exception is thrown when saving null audit")
    void shouldThrowExceptionWhenSavingNullAudit() {
        assertThatThrownBy(() -> auditRepository.save(null))
                .isInstanceOf(RuntimeException.class);
    }

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

    @Test
    @DisplayName("Empty list is returned when there are no audits")
    void shouldReturnEmptyListWhenNoAudits() {
        List<Audit> audits = auditRepository.findAll();

        assertThat(audits).isEmpty();
    }
}