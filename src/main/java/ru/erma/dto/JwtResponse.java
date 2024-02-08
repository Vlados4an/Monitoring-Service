package ru.erma.dto;

public record JwtResponse(String username, String accessToken, String refreshToken) {
}
