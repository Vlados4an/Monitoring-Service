package ru.erma.repository;

import java.util.List;

/**
 * This interface represents a repository for audit logs.
 * It provides methods to save an audit log and to retrieve all audit logs.
 *
 * @param <E> the type of the audit log
 */
public interface AuditRepository<E> {
    /**
     * Saves the given audit log.
     *
     * @param auditLog the audit log to save
     */
    void save(E auditLog);

    /**
     * Retrieves all audit logs.
     *
     * @return a list of all audit logs
     */
    List<E> findAll();
}
