package ru.erma.in.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.ServletException;
import ru.erma.service.UserService;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Date;

/**
 * The JwtTokenProvider class is responsible for creating and validating JWT (JSON Web Token) tokens.
 * It uses the JJWT library to create and parse JWT tokens.
 * It contains methods to create access and refresh tokens, authenticate a user based on a token, get the login from a token, and validate a token.
 */
public class JwtTokenProvider {
    private final Long access;
    private final Long refresh;
    private final UserService userService;
    private final SecretKey secretKey;

    /**
     * Constructs a new JwtTokenProvider instance with the specified secret, access token duration, refresh token duration, and UserService.
     * It creates a SecretKey from the specified secret.
     *
     * @param secret the secret used to sign the JWT tokens
     * @param access the duration of the access token in milliseconds
     * @param refresh the duration of the refresh token in milliseconds
     * @param userService the UserService used to get user details
     */
    public JwtTokenProvider(String secret, Long access, Long refresh, UserService userService) {
        this.access = access;
        this.refresh = refresh;
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.userService = userService;
    }

    /**
     * Creates and returns an access token for the specified login.
     * The token is signed with the secret key and has an expiration time based on the access token duration.
     *
     * @param login the login for which the access token is created
     * @return the created access token
     */
    public String createAccessToken(String login) {
        Claims claims = Jwts.claims().subject(login).build();
        Date now = new Date();
        Date validity = new Date(now.getTime() + access);
        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(validity)
                .signWith(secretKey)
                .compact();
    }

    /**
     * Creates and returns a refresh token for the specified login.
     * The token is signed with the secret key and has an expiration time based on the refresh token duration.
     *
     * @param login the login for which the refresh token is created
     * @return the created refresh token
     */
    public String createRefreshToken(String login) {
        Claims claims = Jwts.claims().subject(login).build();
        Date now = new Date();
        Date validity = new Date(now.getTime() + refresh);
        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(validity)
                .signWith(secretKey)
                .compact();
    }

    /**
     * Authenticates a user based on the specified token.
     * It validates the token, gets the login from the token, gets the user details from the UserService, and returns an Authentication object.
     * If the token is not valid, it throws an AccessDeniedException.
     *
     * @param token the token used to authenticate the user
     * @return an Authentication object representing the authenticated user
     * @throws AccessDeniedException if the token is not valid
     */
    public Authentication authentication(String token) throws AccessDeniedException {
        if (!validateToken(token)) {
            throw new AccessDeniedException("Access denied!");
        }
        String login = getLoginFromToken(token);
        userService.getByUsername(login);
        return new Authentication(login, true, null);
    }

    /**
     * Gets and returns the login from the specified token.
     * It parses the token and returns the subject from the payload.
     *
     * @param token the token from which the login is gotten
     * @return the login gotten from the token
     */
    private String getLoginFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Validates the specified token and returns whether it is valid.
     * It parses the token and checks if the expiration time in the payload is before the current time.
     * If the token is valid, it returns true. Otherwise, it returns false.
     *
     * @param token the token to be validated
     * @return true if the token is valid, false otherwise
     * @throws RuntimeException if an error occurs during the parsing of the token
     */
    public boolean validateToken(String token) throws RuntimeException {
        Jws<Claims> claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);

        return !claims.getPayload().getExpiration().before(new Date());
    }
}
