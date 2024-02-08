package ru.erma.in.security;


/**
 * The Authentication class is used to represent the authentication status of a user.
 * It contains three fields: username, isAuth, and message.
 * The username field represents the username of the user.
 * The isAuth field represents whether the user is authenticated.
 * The message field represents a message related to the user's authentication status.
 */
public class Authentication {
    private String username;
    private boolean isAuth;
    private String message;

    public Authentication() {
    }

    /**
     * Constructs a new Authentication instance with the specified username, authentication status, and message.
     *
     * @param username the username of the user
     * @param isAuth the authentication status of the user
     * @param message a message related to the user's authentication status
     */
    public Authentication(String username, boolean isAuth, String message) {
        this.username = username;
        this.isAuth = isAuth;
        this.message = message;
    }

    /**
     * Returns the authentication status of the user.
     *
     * @return the authentication status of the user
     */
    public boolean isAuth() {
        return isAuth;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
