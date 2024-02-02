package ru.erma.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.erma.exception.DatabaseException;
import ru.erma.model.Audit;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class AuditRepositoryImplTest extends AbstractRepositoryForTest {
    private AuditRepositoryImpl auditRepository;

    @BeforeEach
    void setUp() {
        super.setUp();
        auditRepository = new AuditRepositoryImpl(connectionProvider);
    }

    @Test
    void shouldSaveAudit() {
        Audit audit = new Audit();
        audit.getAudits().add("testAction");

        assertThatCode(() -> auditRepository.save(audit)).doesNotThrowAnyException();
    }

    @Test
    void shouldThrowExceptionWhenSavingNullAudit() {
        assertThatThrownBy(() -> auditRepository.save(null))
                .isInstanceOf(DatabaseException.class);
    }

    @Test
    void shouldFindAllAudits() {
        Audit audit = new Audit();
        audit.getAudits().add("testAction");
        auditRepository.save(audit);

        List<Audit> audits = auditRepository.findAll();

        assertThat(audits).isNotEmpty();
        assertThat(audits.get(0).getAudits().get(0)).isEqualTo("testAction");
    }

    @Test
    void shouldReturnEmptyListWhenNoAudits() {
        List<Audit> audits = auditRepository.findAll();

        assertThat(audits).isEmpty();
    }
}