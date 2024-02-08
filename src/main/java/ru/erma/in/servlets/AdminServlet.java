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
import ru.erma.dto.ReadingTypeRequest;
import ru.erma.dto.SuccessResponse;
import ru.erma.exception.AuthorizeException;
import ru.erma.exception.NotValidArgumentException;
import ru.erma.exception.TypeNotFoundException;
import ru.erma.in.security.Authentication;
import ru.erma.model.Audit;
import ru.erma.service.AuditService;
import ru.erma.service.ReadingStructureService;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {
    private ObjectMapper jacksonMapper;
    private ReadingStructureService readingService;
    private AuditService auditService;

    @Override
    public void init() {
        jacksonMapper = (ObjectMapper) getServletContext().getAttribute("jacksonMapper");
        readingService = (ReadingStructureService) getServletContext().getAttribute("readingStructureService");
        auditService = (AuditService) getServletContext().getAttribute("auditService");
    }

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
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse("Endpoint not found"));
        }
    }

    private void showAudits(HttpServletRequest req, HttpServletResponse resp,Authentication authentication) throws IOException {
        String username = req.getParameter("username");
        if(username == null) throw new NotValidArgumentException("Username parameter is null");
        if (!authentication.getUsername().equals(username) || !username.equals("admin")) throw new AuthorizeException("Incorrect credentials.");
        List<Audit> audits = auditService.getAllAudits();
        resp.setStatus(HttpServletResponse.SC_OK);
        jacksonMapper.writeValue(resp.getWriter(), audits);
    }

    private void addReadingType(HttpServletRequest req, HttpServletResponse resp,Authentication authentication) throws IOException {
        String username = req.getParameter("username");
        if(username == null) throw new NotValidArgumentException("Username parameter is null");
        if (!authentication.getUsername().equals(username) || !username.equals("admin")) throw new AuthorizeException("Incorrect credentials.");
        try(ServletInputStream inputStream = req.getInputStream()) {
            ReadingTypeRequest request = jacksonMapper.readValue(inputStream, ReadingTypeRequest.class);
            readingService.addReadingType(request.type());
        }
        resp.setStatus(HttpServletResponse.SC_CREATED);
        jacksonMapper.writeValue(resp.getWriter(), new SuccessResponse("Reading type added successfully!"));
    }

    private void removeReadingType(HttpServletRequest req, HttpServletResponse resp,Authentication authentication) throws IOException {
        String username = req.getParameter("username");
        if(username == null) throw new NotValidArgumentException("Username parameter is null");
        boolean removed;
        if (!authentication.getUsername().equals(username) || !username.equals("admin")) throw new AuthorizeException("Incorrect credentials.");
        try(ServletInputStream inputStream = req.getInputStream()) {
            ReadingTypeRequest request = jacksonMapper.readValue(inputStream, ReadingTypeRequest.class);
            removed = readingService.removeReadingType(request.type());
        }
        if (removed) {
            resp.setStatus(HttpServletResponse.SC_OK);
            jacksonMapper.writeValue(resp.getWriter(), new SuccessResponse("Reading type removed successfully!"));
        } else {
            throw new TypeNotFoundException("Reading type not found");
        }
    }
}