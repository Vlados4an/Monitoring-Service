package ru.erma.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * The UserEntity class represents users in the system.
 * Each user has a username, password, and a role.
 */
@Getter
@Setter
@Entity(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
    private Long id;

    private String username;

    private String password;

    private String role;
}
