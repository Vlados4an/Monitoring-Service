package ru.erma.in.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * This class is a custom entry point for authentication.
 * It implements the AuthenticationEntryPoint interface provided by Spring Security.
 * When an AuthenticationException is thrown, the commence method is called to handle the exception.
 * The exception handling logic is delegated to the CustomSecurityExceptionHandler.
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final CustomSecurityExceptionHandler exceptionHandler;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        exceptionHandler.handleSecurityException(request, response, authException, HttpServletResponse.SC_UNAUTHORIZED);
    }
}