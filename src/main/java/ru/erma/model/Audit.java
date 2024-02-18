package ru.erma.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class Audit {
   String username;

   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
   LocalDateTime timestamp;

   private String action;
}
