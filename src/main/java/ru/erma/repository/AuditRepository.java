package ru.erma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.erma.model.Audit;

public interface AuditRepository extends JpaRepository<Audit, Long> {

}
