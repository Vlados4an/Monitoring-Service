package ru.erma.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


/**
 * The Audit class represents an audit record in the system.
 * Each instance of this class corresponds to a single action performed by a user at a specific time.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "audits")
public class Audit {


   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "audit_id_seq")
   @SequenceGenerator(name = "audit_id_seq", sequenceName = "audit_id_seq", allocationSize = 1)
   private Long id;

   String username;

   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
   LocalDateTime timestamp;

   private String action;
}
