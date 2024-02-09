package ru.erma.in.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.erma.dto.AdminRequest;
import ru.erma.dto.ExceptionResponse;
import ru.erma.dto.SuccessResponse;
import ru.erma.in.security.Authentication;
import ru.erma.model.Audit;
import ru.erma.service.AuditService;
import ru.erma.service.ReadingStructureService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for AdminServlet.
 * Uses Mockito for mocking dependencies and JUnit for running the tests.
 */
@ExtendWith(MockitoExtension.class)
class AdminServletTest {

    @Mock
    private ReadingStructureService readingService;

    @Mock
    private AuditService auditService;

    @Mock
    private ObjectMapper jacksonMapper;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private PrintWriter stringWriter;

    @Mock
    private ServletInputStream inputStream;

    @Mock
    private ServletContext servletContext;

    @InjectMocks
    private AdminServlet adminServlet;

    /**
     * Set up method for each test.
     * Initializes the AdminServlet and injects the mocked dependencies.
     */
    @BeforeEach
    void setUp() throws ServletException {
        adminServlet = new AdminServlet() {
            public ServletContext getServletContext() {
                return servletContext;
            }
        };

        adminServlet.setJacksonMapper(jacksonMapper);
        adminServlet.setAuditService(auditService);
        adminServlet.setReadingService(readingService);
    }

    /**
     * Test for the doGet method when the user is authenticated and requests audits.
     * The expected behavior is that the server should return an HTTP 200 OK status and a list of audits.
     */
    @Test
    @DisplayName("doGet returns audits when user is authenticated")
    void doGet_returnsAuditsWhenUserIsAuthenticated() throws IOException {
        when(servletContext.getAttribute("authentication")).thenReturn(new Authentication("admin", true, null));
        when(request.getPathInfo()).thenReturn("/audits");
        when(request.getParameter("username")).thenReturn("admin");
        when(auditService.getAllAudits()).thenReturn(List.of(new Audit()));
        when(response.getWriter()).thenReturn(stringWriter);

        adminServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(jacksonMapper).writeValue(any(PrintWriter.class), anyList());
    }

    /**
     * Test for the doGet method when the user is not authenticated.
     * The expected behavior is that the server should return an HTTP 401 Unauthorized status and an ExceptionResponse.
     */
    @Test
    @DisplayName("doGet returns unauthorized when user is not authenticated")
    void doGet_returnsUnauthorizedWhenUserIsNotAuthenticated() throws IOException {
        when(servletContext.getAttribute("authentication")).thenReturn(new Authentication("admin", false, "Unauthorized"));
        when(response.getWriter()).thenReturn(stringWriter);

        adminServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(jacksonMapper).writeValue(any(PrintWriter.class), any(ExceptionResponse.class));
    }

    /**
     * Test for the doGet method when the endpoint does not exist.
     * The expected behavior is that the server should return an HTTP 404 Not Found status and an ExceptionResponse.
     */
    @Test
    @DisplayName("doGet returns not found when endpoint does not exist")
    void doGet_returnsNotFoundWhenEndpointDoesNotExist() throws IOException {
        when(servletContext.getAttribute("authentication")).thenReturn(new Authentication("admin", true, null));
        when(request.getPathInfo()).thenReturn("/nonexistent");
        when(response.getWriter()).thenReturn(stringWriter);

        adminServlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        verify(jacksonMapper).writeValue(any(PrintWriter.class), any(ExceptionResponse.class));
    }

    /**
     * Test for the doPost method when the user is authenticated and adds a reading type.
     * The expected behavior is that the server should return an HTTP 201 Created status and a SuccessResponse.
     */
    @Test
    @DisplayName("doPost adds reading type when user is authenticated")
    void doPost_addsReadingTypeWhenUserIsAuthenticated() throws IOException {
        when(servletContext.getAttribute("authentication")).thenReturn(new Authentication("admin", true, null));
        when(request.getPathInfo()).thenReturn("/add"); // Ensure the path info is "/add"

        AdminRequest adminRequest = new AdminRequest("admin", "newType");

        when(request.getInputStream()).thenReturn(inputStream);
        when(jacksonMapper.readValue(inputStream, AdminRequest.class)).thenReturn(adminRequest);
        when(response.getWriter()).thenReturn(stringWriter);

        adminServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(jacksonMapper).writeValue(any(PrintWriter.class), any(SuccessResponse.class));
    }

    /**
     * Test for the doPost method when the user is authenticated and removes a reading type.
     * The expected behavior is that the server should return an HTTP 200 OK status and a SuccessResponse.
     */
    @Test
    @DisplayName("doPost removes reading type when user is authenticated")
    void doPost_removesReadingTypeWhenUserIsAuthenticated() throws IOException {
        when(servletContext.getAttribute("authentication")).thenReturn(new Authentication("admin", true, null));
        when(request.getPathInfo()).thenReturn("/remove"); // Ensure the path info is "/add"

        AdminRequest adminRequest = new AdminRequest("admin", "existingType");

        when(request.getInputStream()).thenReturn(inputStream);
        when(jacksonMapper.readValue(inputStream, AdminRequest.class)).thenReturn(adminRequest);
        when(readingService.removeReadingType(adminRequest.type())).thenReturn(true);
        when(response.getWriter()).thenReturn(stringWriter);

        adminServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(jacksonMapper).writeValue(any(PrintWriter.class), any(SuccessResponse.class));
    }

    /**
     * Test for the doPost method when the user is not authenticated.
     * The expected behavior is that the server should return an HTTP 401 Unauthorized status and an ExceptionResponse.
     */
    @Test
    @DisplayName("doPost throws exception when user is not authenticated")
    void doPost_throwsExceptionWhenUserIsNotAuthenticated() throws IOException {
        when(servletContext.getAttribute("authentication")).thenReturn(new Authentication("admin", false, "Unauthorized"));
        when(response.getWriter()).thenReturn(stringWriter);

        adminServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(jacksonMapper).writeValue(any(PrintWriter.class), any(ExceptionResponse.class));
    }

    /**
     * Test for the doPost method when the reading type does not exist.
     * The expected behavior is that the server should return an HTTP 400 Bad Request status and an ExceptionResponse.
     */
    @Test
    @DisplayName("doPost throws exception when reading type does not exist")
    void doPost_throwsExceptionWhenReadingTypeDoesNotExist() throws IOException {
        when(servletContext.getAttribute("authentication")).thenReturn(new Authentication("admin", true, null));
        when(request.getPathInfo()).thenReturn("/remove"); // Ensure the path info is "/add"

        AdminRequest adminRequest = new AdminRequest("admin", "nonExistingType");

        when(request.getInputStream()).thenReturn(inputStream);
        when(jacksonMapper.readValue(inputStream, AdminRequest.class)).thenReturn(adminRequest);
        when(readingService.removeReadingType(adminRequest.type())).thenReturn(false);
        when(response.getWriter()).thenReturn(stringWriter);

        adminServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(jacksonMapper).writeValue(any(PrintWriter.class), any(ExceptionResponse.class));
    }
}