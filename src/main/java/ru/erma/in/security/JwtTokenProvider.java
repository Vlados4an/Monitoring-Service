package ru.erma.in.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import ru.erma.model.Role;
import ru.erma.model.User;
import ru.erma.service.UserService;

import javax.crypto.SecretKey;
import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.List;

/**
 * The JwtTokenProvider class is responsible for creating and validating JWT (JSON Web Token) tokens.
 * It uses the JJWT library to create and parse JWT tokens.
 * It contains methods to create access and refresh tokens, authenticate a user based on a token, get the login from a token, and validate a token.
 */
@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties properties;
    private final UserService userService;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(properties.getSecret().getBytes());
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
        String username = getLoginFromToken(token);
        Role role = getRoleFromToken(token);
        User userDetails = userService.loadUserByUsername(username);

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    private Role getRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return Role.valueOf((String) claims.get("role"));
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

    /**
     * Creates and returns an access token for the specified login.
     * The token is signed with the secret key and has an expiration time based on the access token duration.
     * This method calls the private method createToken, passing the login and the access token duration.
     *
     * @param login the login for which the access token is created
     * @return the created access token
     */
    public String createAccessToken(String login,Role role) {
        return createToken(login, properties.getAccess(),role);
    }

    /**
     * Creates and returns a refresh token for the specified login.
     * The token is signed with the secret key and has an expiration time based on the refresh token duration.
     * This method calls the private method createToken, passing the login and the refresh token duration.
     *
     * @param login the login for which the refresh token is created
     * @return the created refresh token
     */
    public String createRefreshToken(String login,Role role) {
        return createToken(login, properties.getRefresh(),role);
    }

    /**
     * Creates and returns a JWT token for the specified login and duration.
     * The token is signed with the secret key and has an expiration time based on the provided duration.
     * The token's claims include the subject (set to the provided login), the issued-at date (set to the current time),
     * and the expiration date (set to the current time plus the provided duration).
     *
     * @param login the login for which the token is created
     * @param duration the duration of the token in milliseconds
     * @return the created token
     */
    private String createToken(String login, Long duration, Role role) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + duration);

        return Jwts.builder()
                .subject(login)
                .claim("role", role.name())
                .issuedAt(now)
                .expiration(validity)
                .signWith(secretKey)
                .compact();
    }

}
