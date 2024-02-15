package ru.erma.dto;

/**
 * The JwtResponse class is a record class in Java.
 * It is used to represent a response that contains JWT (JSON Web Token) information.
 * It contains three fields: username, accessToken, and refreshToken.
 * The username field represents the username of the user for whom the JWTs are issued.
 * The accessToken field represents the access token that can be used to authenticate requests.
 * The refreshToken field represents the refresh token that can be used to obtain a new access token.
 */
public record JwtResponse(String username, String accessToken, String refreshToken) {
}
