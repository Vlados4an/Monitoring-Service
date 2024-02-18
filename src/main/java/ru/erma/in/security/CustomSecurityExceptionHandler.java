package ru.erma.in.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.erma.dto.ErrorResponse;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * This class is used to handle security exceptions.
 * It creates an ErrorResponse with the exception details and writes it to the response.
 */
@Component
@RequiredArgsConstructor
public class CustomSecurityExceptionHandler {
    private final ObjectMapper objectMapper;

    public void handleSecurityException(HttpServletRequest request, HttpServletResponse response,
                                                Exception ex, int status) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(
                status,
                LocalDateTime.now(),
                ex.getMessage(),
                request.getRequestURI()
        );
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}