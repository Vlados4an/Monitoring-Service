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


@WebServlet({"/readings/*"})
public class ReadingServlet extends HttpServlet {
    private ObjectMapper jacksonMapper;
    private ReadingService readingService;
    @Override
    public void init() {
        jacksonMapper = (ObjectMapper) getServletContext().getAttribute("jacksonMapper");
        readingService = (ReadingService) getServletContext().getAttribute("readingService");
    }
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
    private void showActualReadingsProcess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException {
        String username = req.getParameter("username");
        if(username == null) throw new NotValidArgumentException("Username parameter is null");
        if (!authentication.getUsername().equals(username)) throw new AuthorizeException("Incorrect credentials.");
        Reading actualReading = readingService.getActualReadings(username);
        resp.setStatus(HttpServletResponse.SC_OK);
        jacksonMapper.writeValue(resp.getWriter(), actualReading);
    }
    private void showReadingHistory(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException {
        String username = req.getParameter("username");
        if(username == null) throw new NotValidArgumentException("Username parameter is null");
        if (!authentication.getUsername().equals(username)) throw new AuthorizeException("Incorrect credentials.");
        List<Reading> readingHistory = readingService.getReadingHistory(username);
        resp.setStatus(HttpServletResponse.SC_OK);
        jacksonMapper.writeValue(resp.getWriter(), readingHistory);
    }
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
