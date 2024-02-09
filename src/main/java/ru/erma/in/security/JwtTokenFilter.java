package ru.erma.in.security;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * The JwtTokenFilter class implements the Filter interface and is used to validate JWT (JSON Web Token) in the Authorization header of HTTP requests.
 * It is annotated with @WebFilter, which means it is automatically registered and applied to every request in the application.
 */
@WebFilter
public class JwtTokenFilter implements Filter {

    private JwtTokenProvider jwtTokenProvider;

    private ServletContext servletContext;

    /**
     * This method is called by the container to indicate to a filter that it is being placed into service.
     * It initializes the JwtTokenProvider and ServletContext.
     *
     * @param filterConfig a filter configuration object used by a servlet container to pass information to a filter during initialization
     * @throws ServletException if an exception occurs that interrupts the filter's normal operation
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.servletContext = filterConfig.getServletContext();
        jwtTokenProvider = (JwtTokenProvider) servletContext.getAttribute("tokenProvider");
    }

    /**
     * This method is called by the container each time a request/response pair is passed through the chain due to a client request for a resource at the end of the chain.
     * It extracts the JWT from the Authorization header of the request, validates it, and sets the authentication status in the ServletContext.
     * If the JWT is null or invalid, it sets the authentication status to false with an appropriate message.
     * If an exception occurs during the validation of the JWT, it sets the authentication status to false with the exception message.
     *
     * @param servletRequest the ServletRequest object contains the client's request
     * @param servletResponse the ServletResponse object contains the servlet's response
     * @param filterChain the FilterChain for invoking the next filter or the resource
     * @throws IOException if an I/O error occurs during this filter's processing of the request
     * @throws ServletException if the processing fails for any other reason
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String bearerToken = ((HttpServletRequest)servletRequest).getHeader("Authorization");
        try {
            if (bearerToken != null && bearerToken.startsWith("Bearer ") && jwtTokenProvider.validateToken(bearerToken.substring(7))) {
                Authentication authentication = jwtTokenProvider.authentication(bearerToken.substring(7));
                servletContext.setAttribute("authentication", authentication);
            } else {
                servletContext.setAttribute("authentication", new Authentication(null, false, "Bearer token is null or invalid!"));
            }
        } catch (RuntimeException e) {
            servletContext.setAttribute("authentication", new Authentication(null, false, e.getMessage()));
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
