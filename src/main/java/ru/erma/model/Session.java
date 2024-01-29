package ru.erma.model;

import lombok.Getter;
import lombok.Setter;

/**
 * This class represents a Session in the system.
 * A Session has a username and a loggedIn flag.
 */
@Getter
@Setter
public class Session {
    private String username;
    private boolean loggedIn;
}
