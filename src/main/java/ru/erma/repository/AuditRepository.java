package ru.erma.repository;

import java.util.List;

/**
 * This interface represents a repository for audit.
 * It provides methods to save an audit and to retrieve all audits.
 *
 * @param <E> the type of the audit
 */
public interface AuditRepository<E> {
    /**
     * Saves the given audit.
     *
     * @param audit the audit to save
     */
    void save(E audit);

    /**
     * Retrieves all audits.
     *
     * @return a list of all audits
     */
    List<E> findAll();
}
