package ru.erma.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEntity {
    private String username;
    private String password;
    private String role;
}
