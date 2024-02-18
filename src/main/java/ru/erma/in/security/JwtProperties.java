package ru.erma.in.security;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ru.erma.util.YamlPropertySourceFactory;


/**
 * This class represents the properties for JWT (JSON Web Token) configuration.
 */
@Component
@Data
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
public class JwtProperties {

    /**
     * The secret key used for signing the JWT.
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * The expiration time (in milliseconds) of the access token.
     */
    @Value("${jwt.access}")
    private Long access;

    /**
     * The expiration time (in milliseconds) of the refresh token.
     */
    @Value("${jwt.refresh}")
    private Long refresh;
}
