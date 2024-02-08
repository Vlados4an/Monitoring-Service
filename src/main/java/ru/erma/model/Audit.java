package ru.erma.model;


import java.util.ArrayList;
import java.util.List;

/**
 * The Audit class represent audits in the system.
 * It contains a list of audit entries, each representing an action performed in the system.
 * It also contains an id for the audit.
 */
public class Audit {
    Long id;
    private final List<String> audits;
    /**
     * Default constructor for the Audit class.
     * Initializes the list of audit entries.
     */
    public Audit(){
        this.audits = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getAudits() {
        return audits;
    }


}
