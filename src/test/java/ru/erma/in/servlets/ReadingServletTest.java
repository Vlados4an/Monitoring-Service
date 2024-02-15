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
import ru.erma.dto.ExceptionResponse;
import ru.erma.dto.ReadingRequest;
import ru.erma.dto.SuccessResponse;
import ru.erma.exception.NotValidArgumentException;
import ru.erma.in.security.Authentication;
import ru.erma.model.Reading;
import ru.erma.service.ReadingService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for ReadingServlet.
 * Uses Mockito for mocking dependencies and JUnit for running the tests.
 */
@ExtendWith(MockitoExtension.class)
class ReadingServletTest {

    @Mock
    private ReadingService readingService;

    @Mock
    private ObjectMapper jacksonMapper;

    @InjectMocks
    private ReadingServlet readingServlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletInputStream inputStream;

    @Mock
    private PrintWriter printWriter;

    @Mock
    private ServletContext servletContext;

    /**
     * Set up method for each test.
     * Initializes the ReadingServlet and injects the mocked dependencies.
     */
    @BeforeEach
    void setUp() throws ServletException {
        readingServlet = new ReadingServlet() {
            public ServletContext getServletContext() {
                return servletContext;
            }
        };

        readingServlet.setJacksonMapper(jacksonMapper);
        readingServlet.setReadingService(readingService);
    }

    /**
     * Test for the doGet method when the user is authenticated and requests actual readings.
     * The expected behavior is that the server should return an HTTP 200 OK status and a Reading object.
     */
    @Test
    @DisplayName("doGet returns actual readings when user is authenticated")
    void doGet_returnsActualReadingsWhenUserIsAuthenticated() throws IOException {
        String username = "test";

        when(servletContext.getAttribute("authentication")).thenReturn(new Authentication(username, true, null));
        when(request.getPathInfo()).thenReturn("/actual"); // Ensure the path info is "/audits"
        when(request.getParameter("username")).thenReturn(username);
        when(readingService.getActualReadings(username)).thenReturn(new Reading());
        when(response.getWriter()).thenReturn(printWriter);

        readingServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(jacksonMapper).writeValue(any(PrintWriter.class), any(Reading.class));
    }

    /**
     * Test for the doGet method when the user is authenticated and requests reading history.
     * The expected behavior is that the server should return an HTTP 200 OK status and a list of Reading objects.
     */
    @Test
    @DisplayName("doGet returns reading history when user is authenticated")
    void doGet_returnsReadingHistoryWhenUserIsAuthenticated() throws IOException {
        String username = "testUser";
        List<Reading> readingHistory = List.of(new Reading());

        when(servletContext.getAttribute("authentication")).thenReturn(new Authentication(username, true, null));
        when(request.getPathInfo()).thenReturn("/history"); // Ensure the path info is "/audits"
        when(request.getParameter("username")).thenReturn(username);
        when(readingService.getReadingHistory(username)).thenReturn(readingHistory);
        when(response.getWriter()).thenReturn(printWriter);

        readingServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(jacksonMapper).writeValue(any(PrintWriter.class), eq(readingHistory));
    }

    /**
     * Test for the doGet method when the user is authenticated and requests readings for a specific month.
     * The expected behavior is that the server should return an HTTP 200 OK status and a list of Reading objects.
     */
    @Test
    @DisplayName("doGet returns readings for month when user is authenticated")
    void doGet_returnsReadingsForMonthWhenUserIsAuthenticated() throws IOException {
        String username = "testUser";
        int month = 10;
        int year = 2000;
        when(servletContext.getAttribute("authentication")).thenReturn(new Authentication(username, true, null));
        when(request.getPathInfo()).thenReturn("/month"); // Ensure the path info is "/audits"
        List<Reading> readingsForMonth = List.of(new Reading());

        when(request.getParameter("username")).thenReturn(username);
        when(request.getParameter("month")).thenReturn(String.valueOf(month));
        when(request.getParameter("year")).thenReturn(String.valueOf(year));
        when(readingService.getReadingsForMonth(username, month, year)).thenReturn(readingsForMonth);
        when(response.getWriter()).thenReturn(printWriter);

        readingServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(jacksonMapper).writeValue(any(PrintWriter.class), eq(readingsForMonth));
    }

    /**
     * Test for the doGet method when the user is not authenticated.
     * The expected behavior is that the server should return an HTTP 401 Unauthorized status and an ExceptionResponse.
     */
    @Test
    @DisplayName("doGet returns unauthorized when user is not authenticated")
    void doGet_returnsUnauthorizedWhenUserIsNotAuthenticated() throws IOException {
        when(servletContext.getAttribute("authentication")).thenReturn(new Authentication("testUser", false, "Unauthorized"));
        when(response.getWriter()).thenReturn(printWriter);

        readingServlet.doGet(request, response);

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
        when(response.getWriter()).thenReturn(printWriter);

        readingServlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        verify(jacksonMapper).writeValue(any(PrintWriter.class), any(ExceptionResponse.class));
    }

    /**
     * Test for the doPost method when the user is authenticated and submits readings.
     * The expected behavior is that the server should return an HTTP 201 Created status and a SuccessResponse.
     */
    @Test
    @DisplayName("doPost submits readings when user is authenticated")
    void doPost_submitsReadingsWhenUserIsAuthenticated() throws IOException {
        String username = "testUser";
        when(servletContext.getAttribute("authentication")).thenReturn(new Authentication(username, true, null));
        when(request.getPathInfo()).thenReturn("/submit");

        ReadingRequest readingRequest = new ReadingRequest(username, 1, 2022, new HashMap<>());

        when(request.getInputStream()).thenReturn(inputStream);
        when(jacksonMapper.readValue(inputStream, ReadingRequest.class)).thenReturn(readingRequest);
        when(response.getWriter()).thenReturn(printWriter);

        readingServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(jacksonMapper).writeValue(any(PrintWriter.class), any(SuccessResponse.class));
    }

    /**
     * Test for the doPost method when the user is not authenticated.
     * The expected behavior is that the server should return an HTTP 401 Unauthorized status and an ExceptionResponse.
     */
    @Test
    @DisplayName("doPost returns unauthorized when user is not authenticated")
    void doPost_returnsUnauthorizedWhenUserIsNotAuthenticated() throws IOException {
        when(servletContext.getAttribute("authentication")).thenReturn(new Authentication("admin", false, "Unauthorized"));
        when(request.getPathInfo()).thenReturn("/submit");
        when(response.getWriter()).thenReturn(printWriter);

        readingServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(jacksonMapper).writeValue(any(PrintWriter.class), any(ExceptionResponse.class));
    }

    /**
     * Test for the doPost method when the endpoint does not exist.
     * The expected behavior is that the server should return an HTTP 404 Not Found status and an ExceptionResponse.
     */
    @Test
    @DisplayName("doPost returns not found when endpoint does not exist")
    void doPost_returnsNotFoundWhenEndpointDoesNotExist() throws IOException {
        when(response.getWriter()).thenReturn(printWriter);

        readingServlet.doPost(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        verify(jacksonMapper).writeValue(any(PrintWriter.class), any(ExceptionResponse.class));
    }

    /**
     * Test for the doPost method when the request body is invalid.
     * The expected behavior is that the server should return an HTTP 400 Bad Request status and an ExceptionResponse.
     */
    @Test
    @DisplayName("doPost returns bad request when request body is invalid")
    void doPost_returnsBadRequestWhenRequestBodyIsInvalid() throws IOException {
        when(servletContext.getAttribute("authentication")).thenReturn(new Authentication("admin", true, null));
        when(request.getPathInfo()).thenReturn("/submit");
        when(request.getInputStream()).thenReturn(inputStream);
        when(jacksonMapper.readValue(inputStream, ReadingRequest.class)).thenThrow(new NotValidArgumentException(""));
        when(response.getWriter()).thenReturn(printWriter);

        readingServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(jacksonMapper).writeValue(any(PrintWriter.class), any(ExceptionResponse.class));
    }
}