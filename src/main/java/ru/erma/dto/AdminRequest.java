package ru.erma.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Data object representing a request made by an admin in the system")
public record AdminRequest(
        @Schema(description = "Type of the reading to be added by the admin", example = "temperature")
        @NotBlank(message = "type should not be blank") String type) {
}