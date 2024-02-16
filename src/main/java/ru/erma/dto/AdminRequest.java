package ru.erma.dto;

import javax.validation.constraints.NotBlank;

/**
 * The AdminRequest class is a record class in Java.
 * It is used to represent a request made by an admin in the system.
 * It contains two fields: username and type.
 * The username field represents the username of the admin making the request.
 * The type field represents the type of reading to be added by the admin.
 */
public record AdminRequest(
        @NotBlank(message = "Username must not be blank.") String username,
        @NotBlank(message = "type should not be blank") String type) {
}
