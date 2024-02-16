package ru.erma.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


/**
 * The Audit class represent audits in the system.
 * It contains a list of audit entries, each representing an action performed in the system.
 * It also contains an id for the audit.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Audit {
   String username;
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
   LocalDateTime timestamp;
   private String action;
}
