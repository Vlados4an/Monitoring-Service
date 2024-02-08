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

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    private SecurityService securityService;
    private ObjectMapper jacksonMapper;
    @Override
    public void init() {
        securityService = (SecurityService) getServletContext().getAttribute("securityService");
        jacksonMapper = (ObjectMapper) getServletContext().getAttribute("jacksonMapper");
    }

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
