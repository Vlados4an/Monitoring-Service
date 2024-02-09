package ru.erma.in.servlets;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.erma.dto.*;
import ru.erma.exception.AuthorizeException;
import ru.erma.exception.NotValidArgumentException;
import ru.erma.exception.TypeNotFoundException;
import ru.erma.in.security.Authentication;
import ru.erma.model.Audit;
import ru.erma.service.AuditService;
import ru.erma.service.ReadingStructureService;

import java.io.IOException;
import java.util.List;

/**
 * The AdminServlet class extends the HttpServlet class and is used to handle HTTP requests related to admin operations.
 * It is annotated with @WebFilter, which means it is automatically registered and mapped to the "/admin/*" URL pattern.
 * It contains methods to handle GET and POST requests, as well as helper methods to show audits, add reading types, and remove reading types.
 */
@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {
    private ObjectMapper jacksonMapper;
    private ReadingStructureService readingService;
    private AuditService auditService;


    /**
     * This method is called by the servlet container to indicate to this servlet that the servlet is being placed into service.
     * It initializes the ObjectMapper, ReadingStructureService, and AuditService from the ServletContext.
     */
    @Override
    public void init() {
        jacksonMapper = (ObjectMapper) getServletContext().getAttribute("jacksonMapper");
        readingService = (ReadingStructureService) getServletContext().getAttribute("readingStructureService");
        auditService = (AuditService) getServletContext().getAttribute("auditService");
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
        if(authentication.isAuth()) {
            try {
                String pathInfo = req.getPathInfo();
                if ("/audits".equals(pathInfo)) {
                    showAudits(req, resp,authentication);
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse("Endpoint not found"));
                }
            }catch (NotValidArgumentException e) {
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
     * It checks the authentication status, validates the request path, and calls the appropriate helper method based on the path.
     * It also handles exceptions and sends appropriate responses.
     *
     * @param req an HttpServletRequest object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws IOException if an input or output error is detected when the servlet handles the POST request
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");
        if(authentication.isAuth()){
            try {
                String pathInfo = req.getPathInfo();
                if ("/add".equals(pathInfo)) {
                    addReadingType(req, resp,authentication);
                } else if ("/remove".equals(pathInfo)) {
                    removeReadingType(req, resp,authentication);
                }
                else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse("Endpoint not found"));
                }
            }catch (TypeNotFoundException | NotValidArgumentException
                    | JsonParseException | InvalidFormatException e) {
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
     * This helper method is used to show audits.
     * It validates the username parameter, checks the authentication status, gets all audits from the AuditService, and sends a response with the audits.
     *
     * @param req an HttpServletRequest object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @param authentication an Authentication object that contains the authentication status of the user
     * @throws IOException if an input or output error is detected when the servlet handles the request
     */
    private void showAudits(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException {
        String username = req.getParameter("username");
        if(username == null) throw new NotValidArgumentException("Username parameter is null");
        if (!authentication.getUsername().equals(username) || !username.equals("admin")) throw new AuthorizeException("Incorrect credentials.");
        List<Audit> audits = auditService.getAllAudits();
        resp.setStatus(HttpServletResponse.SC_OK);
        jacksonMapper.writeValue(resp.getWriter(), audits);
    }

    /**
     * This helper method is used to add a reading type.
     * It validates the username parameter, checks the authentication status, adds the reading type using the ReadingStructureService, and sends a success response.
     *
     * @param req an HttpServletRequest object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @param authentication an Authentication object that contains the authentication status of the user
     * @throws IOException if an input or output error is detected when the servlet handles the request
     */
    private void addReadingType(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException {
        try(ServletInputStream inputStream = req.getInputStream()) {
            AdminRequest request = jacksonMapper.readValue(inputStream, AdminRequest.class);

            String username = request.username();
            if (!authentication.getUsername().equals(username) || !username.equals("admin")) throw new AuthorizeException("Incorrect credentials.");

            readingService.addReadingType(request.type());
        }
        resp.setStatus(HttpServletResponse.SC_CREATED);
        jacksonMapper.writeValue(resp.getWriter(), new SuccessResponse("Reading type added successfully!"));
    }

    /**
     * This helper method is used to remove a reading type.
     * It validates the username parameter, checks the authentication status, removes the reading type using the ReadingStructureService, and sends a success response if the reading type was removed.
     *
     * @param req an HttpServletRequest object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @param authentication an Authentication object that contains the authentication status of the user
     * @throws IOException if an input or output error is detected when the servlet handles the request
     */
    private void removeReadingType(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException {
        boolean removed;

        try(ServletInputStream inputStream = req.getInputStream()) {
            AdminRequest request = jacksonMapper.readValue(inputStream, AdminRequest.class);

            String username = request.username();
            if (!authentication.getUsername().equals(username) || !username.equals("admin")) throw new AuthorizeException("Incorrect credentials.");

            removed = readingService.removeReadingType(request.type());
        }
        if (removed) {
            resp.setStatus(HttpServletResponse.SC_OK);
            jacksonMapper.writeValue(resp.getWriter(), new SuccessResponse("Reading type removed successfully!"));
        } else {
            throw new TypeNotFoundException("Reading type not found");
        }
    }

    public void setJacksonMapper(ObjectMapper jacksonMapper) {
        this.jacksonMapper = jacksonMapper;
    }

    public void setReadingService(ReadingStructureService readingService) {
        this.readingService = readingService;
    }

    public void setAuditService(AuditService auditService) {
        this.auditService = auditService;
    }
}