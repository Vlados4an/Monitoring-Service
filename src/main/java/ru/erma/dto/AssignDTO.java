package ru.erma.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Data object representing a request to assign a role to a user")
public record AssignDTO(
        @Schema(description = "Username of the user to be assigned a role", example = "john_doe")
        @NotBlank(message = "Username must not be blank.") String username) {
}