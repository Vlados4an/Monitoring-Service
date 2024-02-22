package ru.erma.in.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * This class is a custom handler for AccessDeniedException.
 * It implements the AccessDeniedHandler interface provided by Spring Security.
 * When an AccessDeniedException is thrown, the handle method is called to handle the exception.
 * The exception handling logic is delegated to the CustomSecurityExceptionHandler.
 */
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final CustomSecurityExceptionHandler exceptionHandler;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        exceptionHandler.handleSecurityException(request, response, accessDeniedException, HttpServletResponse.SC_FORBIDDEN);
    }
}