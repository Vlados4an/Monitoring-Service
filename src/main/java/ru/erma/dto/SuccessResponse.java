package ru.erma.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data object representing a response that indicates a successful operation")
public record SuccessResponse(
        @Schema(description = "Success message to be sent in the response", example = "Operation completed successfully")
        String message) {
}