package ru.erma.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Data object representing an error response")
public record ErrorResponse(
        @Schema(description = "HTTP status code of the error", example = "400")
        int statusCode,

        @Schema(description = "Timestamp when the error occurred", example = "2024-02-18 03:15:17")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime timestamp,

        @Schema(description = "Message describing the error", example = "Invalid request body")
        String message,

        @Schema(description = "Description of the error", example = "/readings/actual")
        String description) {}