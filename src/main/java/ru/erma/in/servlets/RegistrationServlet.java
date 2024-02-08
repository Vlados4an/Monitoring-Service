package ru.erma.in.servlets;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.erma.dto.ExceptionResponse;
import ru.erma.dto.SecurityDTO;
import ru.erma.dto.SuccessResponse;
import ru.erma.exception.RegisterException;
import ru.erma.model.User;
import ru.erma.service.SecurityService;

import java.io.IOException;

/**
 * The RegistrationServlet class extends the HttpServlet class and is used to handle HTTP requests related to user registration.
 * It is annotated with @WebServlet, which means it is automatically registered and mapped to the "/register" URL pattern.
 * It contains methods to handle POST requests, which are used for user registration.
 */
@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    private SecurityService securityService;
    private ObjectMapper jacksonMapper;

    /**
     * This method is called by the servlet container to indicate to this servlet that the servlet is being placed into service.
     * It initializes the SecurityService and ObjectMapper from the ServletContext.
     */
    @Override
    public void init() {
        securityService = (SecurityService) getServletContext().getAttribute("securityService");
        jacksonMapper = (ObjectMapper) getServletContext().getAttribute("jacksonMapper");
    }

    /**
     * This method is called by the server (via the service method) to allow a servlet to handle a POST request.
     * It reads a SecurityDTO object from the request body, registers the user using the SecurityService, and sends a SuccessResponse with the username of the registered user.
     * If an exception occurs during the processing of the request, it sends an ExceptionResponse with the appropriate HTTP status code and exception message.
     *
     * @param req an HttpServletRequest object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws IOException if an input or output error is detected when the servlet handles the POST request
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try(ServletInputStream inputStream = req.getInputStream()) {
            SecurityDTO securityDTO = jacksonMapper.readValue(inputStream, SecurityDTO.class);
            User registered = securityService.register(securityDTO.username(), securityDTO.password());

            resp.setStatus(HttpServletResponse.SC_CREATED);
            jacksonMapper.writeValue(resp.getWriter(), new SuccessResponse("Player with username " + registered.getUsername() + " successfully created."));
        } catch (RegisterException | JsonParseException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        }
    }
}
