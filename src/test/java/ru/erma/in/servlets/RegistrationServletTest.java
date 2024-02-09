package ru.erma.in.servlets;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.erma.dto.ExceptionResponse;
import ru.erma.dto.SecurityDTO;
import ru.erma.dto.SuccessResponse;
import ru.erma.exception.RegisterException;
import ru.erma.model.User;
import ru.erma.service.SecurityService;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationServletTest {

    @Mock
    private SecurityService securityService;

    @Mock
    private ObjectMapper jacksonMapper;

    @InjectMocks
    private RegistrationServlet registrationServlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletInputStream inputStream;

    @Mock
    private PrintWriter printWriter;

    @Test
    @DisplayName("doPost returns success response when registration is successful")
    void doPost_returnsSuccessResponseWhenRegistrationIsSuccessful() throws IOException {
        SecurityDTO securityDTO = new SecurityDTO("testUser", "testPass");
        User registeredUser = new User();
        registeredUser.setUsername("testUser");

        when(request.getInputStream()).thenReturn(inputStream);
        when(jacksonMapper.readValue(inputStream, SecurityDTO.class)).thenReturn(securityDTO);
        when(securityService.register(securityDTO)).thenReturn(registeredUser);
        when(response.getWriter()).thenReturn(printWriter);

        registrationServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(jacksonMapper).writeValue(any(PrintWriter.class), any(SuccessResponse.class));
    }

    @Test
    @DisplayName("doPost returns error response when registration fails due to existing user")
    void doPost_returnsErrorResponseWhenRegistrationFailsDueToExistingUser() throws IOException {
        SecurityDTO securityDTO = new SecurityDTO("testUser", "testPass");

        when(request.getInputStream()).thenReturn(inputStream);
        when(jacksonMapper.readValue(inputStream, SecurityDTO.class)).thenReturn(securityDTO);
        when(securityService.register(securityDTO)).thenThrow(RegisterException.class);
        when(response.getWriter()).thenReturn(printWriter);

        registrationServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(jacksonMapper).writeValue(any(PrintWriter.class), any(ExceptionResponse.class));
    }

    @Test
    @DisplayName("doPost returns error response when JsonParseException is thrown")
    void doPost_returnsErrorResponseWhenJsonParseExceptionIsThrown() throws IOException {
        when(request.getInputStream()).thenReturn(inputStream);
        when(jacksonMapper.readValue(inputStream, SecurityDTO.class)).thenThrow(JsonParseException.class);
        when(response.getWriter()).thenReturn(printWriter);

        registrationServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(jacksonMapper).writeValue(any(PrintWriter.class), any(ExceptionResponse.class));
    }

    @Test
    @DisplayName("doPost returns error response when RuntimeException is thrown")
    void doPost_returnsErrorResponseWhenRuntimeExceptionIsThrown() throws IOException {
        SecurityDTO securityDTO = new SecurityDTO("testUser", "testPass");

        when(request.getInputStream()).thenReturn(inputStream);
        when(jacksonMapper.readValue(inputStream, SecurityDTO.class)).thenReturn(securityDTO);
        when(securityService.register(securityDTO)).thenThrow(RuntimeException.class);
        when(response.getWriter()).thenReturn(printWriter);

        registrationServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(jacksonMapper).writeValue(any(PrintWriter.class), any(ExceptionResponse.class));
    }
}