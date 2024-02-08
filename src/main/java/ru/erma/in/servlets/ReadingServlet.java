package ru.erma.in.servlets;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.erma.dto.ExceptionResponse;
import ru.erma.dto.ReadingRequest;
import ru.erma.dto.SuccessResponse;
import ru.erma.exception.*;
import ru.erma.in.security.Authentication;
import ru.erma.model.Reading;
import ru.erma.service.ReadingService;

import java.io.IOException;
import java.util.List;

/**
 * The ReadingServlet class extends the HttpServlet class and is used to handle HTTP requests related to reading operations.
 * It is annotated with @WebServlet, which means it is automatically registered and mapped to the "/readings/*" URL pattern.
 * It contains methods to handle GET and POST requests, as well as helper methods to show actual readings, show reading history, show readings for a specific month, and handle reading submission.
 */
@WebServlet({"/readings/*"})
public class ReadingServlet extends HttpServlet {
    private ObjectMapper jacksonMapper;
    private ReadingService readingService;

    /**
     * This method is called by the servlet container to indicate to this servlet that the servlet is being placed into service.
     * It initializes the ObjectMapper and ReadingService from the ServletContext.
     */
    @Override
    public void init() {
        jacksonMapper = (ObjectMapper) getServletContext().getAttribute("jacksonMapper");
        readingService = (ReadingService) getServletContext().getAttribute("readingService");
    }

    /**
     * This method is called by the server (via the service method) to allow a servlet to handle a GET request.
     * It checks the authentication status, validates the request path, and calls the appropriate helper method based on the path.
     * It also handles exceptions and sends appropriate responses.
     *
     * @param req an HttpServletRequest object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws IOException if an input or output error is detected when the servlet handles the GET request
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");
        if(authentication.isAuth()){
            try{
                String pathInfo = req.getPathInfo();
                if ("/actual".equals(pathInfo)) {
                    showActualReadingsProcess(req, resp,authentication);
                } else if ("/history".equals(pathInfo)) {
                    showReadingHistory(req, resp,authentication);
                } else if ("/month".equals(pathInfo)) {
                    showReadingForMonth(req, resp,authentication);
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse("Endpoint not found"));
                }
            }catch (UserNotFoundException | ReadingNotFoundException | NotValidArgumentException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
            } catch (AuthorizeException e) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
            } catch (RuntimeException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
            }
        }else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse(authentication.getMessage()));
        }

    }

    /**
     * This method is called by the server (via the service method) to allow a servlet to handle a POST request.
     * It checks the request path and calls the handleReadingSubmission helper method if the path is "/submit".
     * If the path is not "/submit", it sends a 404 Not Found error and an ExceptionResponse with the message "Endpoint not found".
     *
     * @param req an HttpServletRequest object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws IOException if an input or output error is detected when the servlet handles the POST request
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if("/submit".equals(pathInfo)){
            handleReadingSubmission(req, resp);
        }else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse("Endpoint not found"));
        }
    }

    /**
     * This helper method is used to handle reading submission.
     * It validates the username parameter, checks the authentication status, gets all audits from the AuditService, and sends a response with the audits.
     *
     * @param req an HttpServletRequest object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws IOException if an input or output error is detected when the servlet handles the request
     */
    private void handleReadingSubmission(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");
        if (authentication.isAuth()) {
            try(ServletInputStream inputStream = req.getInputStream()) {
                ReadingRequest request = jacksonMapper.readValue(inputStream, ReadingRequest.class);

                if (!authentication.getUsername().equals(request.username())) throw new AuthorizeException("Incorrect credentials.");

                readingService.submitReadings(request);

                resp.setStatus(HttpServletResponse.SC_CREATED);
                jacksonMapper.writeValue(resp.getWriter(), new SuccessResponse("Reading submitted successfully!"));
            } catch (UserNotFoundException | ReadingNotFoundException | ReadingAlreadyExistsException | NotValidArgumentException |
                     JsonParseException | InvalidFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
            } catch (AuthorizeException e) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
            } catch (RuntimeException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse(authentication.getMessage()));
        }
    }

    /**
     * This helper method is used to show actual readings.
     * It validates the username parameter, checks the authentication status, gets all audits from the AuditService, and sends a response with the audits.
     *
     * @param req an HttpServletRequest object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @param authentication an Authentication object that contains the authentication status of the user
     * @throws IOException if an input or output error is detected when the servlet handles the request
     */
    private void showActualReadingsProcess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException {
        String username = req.getParameter("username");
        if(username == null) throw new NotValidArgumentException("Username parameter is null");
        if (!authentication.getUsername().equals(username)) throw new AuthorizeException("Incorrect credentials.");
        Reading actualReading = readingService.getActualReadings(username);
        resp.setStatus(HttpServletResponse.SC_OK);
        jacksonMapper.writeValue(resp.getWriter(), actualReading);
    }

    /**
     * This helper method is used to show reading history.
     * It validates the username parameter, checks the authentication status, gets all audits from the AuditService, and sends a response with the audits.
     *
     * @param req an HttpServletRequest object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @param authentication an Authentication object that contains the authentication status of the user
     * @throws IOException if an input or output error is detected when the servlet handles the request
     */
    private void showReadingHistory(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException {
        String username = req.getParameter("username");
        if(username == null) throw new NotValidArgumentException("Username parameter is null");
        if (!authentication.getUsername().equals(username)) throw new AuthorizeException("Incorrect credentials.");
        List<Reading> readingHistory = readingService.getReadingHistory(username);
        resp.setStatus(HttpServletResponse.SC_OK);
        jacksonMapper.writeValue(resp.getWriter(), readingHistory);
    }

    /**
     * This helper method is used to show readings for a specific month.
     * It validates the username parameter, checks the authentication status, gets all audits from the AuditService, and sends a response with the audits.
     *
     * @param req an HttpServletRequest object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @param authentication an Authentication object that contains the authentication status of the user
     * @throws IOException if an input or output error is detected when the servlet handles the request
     */
    private void showReadingForMonth(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException {
        String username = req.getParameter("username");
        if(username == null) throw new NotValidArgumentException("Username parameter is null");
        if (!authentication.getUsername().equals(username)) throw new AuthorizeException("Incorrect credentials.");
        int month;
        int year;
        try {
            month = Integer.parseInt(req.getParameter("month"));
            year = Integer.parseInt(req.getParameter("year"));
        } catch (NumberFormatException e) {
            throw new NotValidArgumentException("Month and year parameters must be integers!");
        }
        List<Reading> readingsForMonth = readingService.getReadingsForMonth(username, month, year);
        resp.setStatus(HttpServletResponse.SC_OK);
        jacksonMapper.writeValue(resp.getWriter(), readingsForMonth);
    }
}
