package ru.erma.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;


/**
 * The Audit class represents an audit record in the system.
 * Each instance of this class corresponds to a single action performed by a user at a specific time.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuditDTO {

    String username;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp;

    private String action;
}
