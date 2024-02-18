package ru.erma.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Data object representing a security data transfer object that contains a username and password")
public record SecurityDTO(
        @Schema(description = "Username of the user", example = "john_doe")
        @NotBlank(message = "Username must not be blank.") String username,

        @Schema(description = "Password of the user", example = "password123")
        @NotBlank(message = "Password must mot be blank.") String password) {
}