package ru.erma.in.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import ru.erma.service.UserService;

import javax.crypto.SecretKey;
import java.nio.file.AccessDeniedException;
import java.util.Date;

public class JwtTokenProvider {
    private final Long access;
    private final Long refresh;
    private final UserService userService;
    private final SecretKey secretKey;

    public JwtTokenProvider(String secret, Long access, Long refresh, UserService userService) {
        this.access = access;
        this.refresh = refresh;
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.userService = userService;
    }

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

    public Authentication authentication(String token) throws AccessDeniedException {
        if (!validateToken(token)) {
            throw new AccessDeniedException("Access denied!");
        }
        String login = getLoginFromToken(token);
        userService.getByUsername(login);
        return new Authentication(login, true, null);
    }

    private String getLoginFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }


    public boolean validateToken(String token) throws RuntimeException {
        Jws<Claims> claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);

        return !claims.getPayload().getExpiration().before(new Date());
    }
}
