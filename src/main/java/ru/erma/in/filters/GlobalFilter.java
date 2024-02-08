package ru.erma.in.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

/**
 * The GlobalFilter class implements the Filter interface and is used to set the character encoding and content type for all requests and responses.
 * It is annotated with @WebFilter, which means it is automatically registered and applied to every request in the application.
 */
@WebFilter
public class GlobalFilter implements Filter {
    /**
     * This method is called by the container each time a request/response pair is passed through the chain due to a client request for a resource at the end of the chain.
     * It sets the character encoding of the request to UTF-8 and the content type of the response to application/json with a charset of UTF-8.
     * It then passes the request and response to the next entity in the filter chain.
     *
     * @param request the ServletRequest object contains the client's request
     * @param response the ServletResponse object contains the servlet's response
     * @param chain the FilterChain for invoking the next filter or the resource
     * @throws IOException if an I/O error occurs during this filter's processing of the request
     * @throws ServletException if the processing fails for any other reason
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        chain.doFilter(request, response);
    }
}
