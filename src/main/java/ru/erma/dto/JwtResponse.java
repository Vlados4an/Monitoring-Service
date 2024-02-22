package ru.erma.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data object representing a response that contains JWT (JSON Web Token) information")
public record JwtResponse(
        @Schema(description = "Username of the user for whom the JWTs are issued", example = "john_doe")
        String username,

        @Schema(description = "Access token that can be used to authenticate requests", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String accessToken,

        @Schema(description = "Refresh token that can be used to obtain a new access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String refreshToken) {
}