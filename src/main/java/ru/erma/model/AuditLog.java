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
public class AuditLog {
    Long id;
    private List<String> logs;

    /**
     * Default constructor for the AuditLog class.
     * Initializes the list of log entries.
     */
    public AuditLog(){
        this.logs = new ArrayList<>();
    }

    /**
     * Returns a string representation of the AuditLog.
     * The string includes the id of the audit log and its log entries.
     *
     * @return a string representation of the AuditLog
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Audit Log ID: ").append(id).append("\n");
        sb.append("Logs: \n");
        for (String log : logs) {
            sb.append(" - ").append(log).append("\n");
        }
        return sb.toString();
    }

}
