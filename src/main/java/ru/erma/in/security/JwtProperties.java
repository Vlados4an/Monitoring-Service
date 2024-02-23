package ru.erma.in.security;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * This class represents the properties for JWT (JSON Web Token) configuration.
 */
@Component
@Data
public class JwtProperties {

    /**
     * The secret key used for signing the JWT.
     */
    @Value("${security.jwt.secret}")
    private String secret;

    /**
     * The expiration time (in milliseconds) of the access token.
     */
    @Value("${security.jwt.access}")
    private Long access;

    /**
     * The expiration time (in milliseconds) of the refresh token.
     */
    @Value("${security.jwt.refresh}")
    private Long refresh;
}
