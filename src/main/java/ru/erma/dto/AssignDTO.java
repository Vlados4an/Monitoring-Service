package ru.erma.dto;

import javax.validation.constraints.NotBlank;

public record AssignDTO(
        @NotBlank(message = "Username must not be blank.") String username) {
}
