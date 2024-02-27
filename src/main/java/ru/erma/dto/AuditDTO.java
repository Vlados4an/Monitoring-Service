package ru.erma.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Data object representing an audit record in the system")
public record AuditDTO(
        @Schema(description = "Username of the user who performed the action", example = "john_doe")
        String username,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @Schema(description = "Timestamp when the action was performed", example = "2022-01-01 12:00:00")
        LocalDateTime timestamp,

        @Schema(description = "Action performed by the user", example = "Login")
        String action){ }