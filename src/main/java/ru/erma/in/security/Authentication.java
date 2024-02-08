package ru.erma.in.security;


public class Authentication {
    private String username;
    private boolean isAuth;
    private String message;

    public Authentication() {
    }

    public Authentication(String username, boolean isAuth, String message) {
        this.username = username;
        this.isAuth = isAuth;
        this.message = message;
    }

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
