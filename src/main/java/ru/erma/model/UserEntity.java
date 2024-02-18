package ru.erma.model;

import lombok.Getter;
import lombok.Setter;

/**
 * The UserEntity class represents users in the system.
 * Each user has a username, password, and a role.
 */
@Getter
@Setter
public class UserEntity {
    private String username;

    private String password;

    private String role;
}
