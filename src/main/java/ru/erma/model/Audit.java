package ru.erma.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an audit log.
 * An audit log contains a list of log entries, each representing an action performed in the system.
 */
@Getter
@Setter
public class Audit {
    Long id;
    private List<String> audits;

    /**
     * Default constructor for the AuditLog class.
     * Initializes the list of log entries.
     */
    public Audit(){
        this.audits = new ArrayList<>();
    }

    /**
     * Returns a string representation of the Audit.
     * The string includes the id of the audit and its log entries.
     *
     * @return a string representation of the Audit
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Audit ID: ").append(id).append("\n");
        sb.append("Logs: \n");
        for (String log : audits) {
            sb.append(" - ").append(log).append("\n");
        }
        return sb.toString();
    }

}
