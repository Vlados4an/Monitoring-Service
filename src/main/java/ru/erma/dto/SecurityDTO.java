package ru.erma.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * The SecurityDTO class is a record class in Java.
 * It is used to represent a security data transfer object that contains a username and password.
 * It contains two fields: username and password.
 * The username field represents the username of the user.
 * The password field represents the password of the user.
 */
public record SecurityDTO(
        @NotBlank(message = "Username must not be blank.") String username,
        @NotBlank(message = "Password must mot be blank.") String password) {
}
