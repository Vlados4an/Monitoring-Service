package ru.erma.in.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * This class is a filter for JWT (JSON Web Token) authentication.
 * It extends the GenericFilterBean class provided by Spring Security.
 * The filter validates the JWT and authenticates the user.
 * If the JWT is expired or malformed, the filter handles the exception by delegating to the CustomSecurityExceptionHandler.
 */
@AllArgsConstructor
@Component
public class JwtTokenFilter extends GenericFilterBean {

    private final CustomSecurityExceptionHandler exceptionHandler;

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * This method is called for each request to validate the JWT and authenticate the user.
     * If the JWT is valid, the user is authenticated and the filter chain continues.
     * If the JWT is expired or malformed, the exception is handled and the filter chain is not continued.
     *
     * @param servletRequest  the servlet request
     * @param servletResponse the servlet response
     * @param filterChain     the filter chain
     * @throws IOException if an input or output error is detected when the handler is handling the exception
     * @throws ServletException if the request could not be handled
     * @throws ExpiredJwtException if the JWT is expired
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException, ExpiredJwtException {
        String bearerToken = ((HttpServletRequest) servletRequest).getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken = bearerToken.substring(7);
        }
        try {
            if (bearerToken != null && jwtTokenProvider.validateToken(bearerToken)) {
                Authentication authentication = jwtTokenProvider.authentication(bearerToken);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (ExpiredJwtException | MalformedJwtException ex) {
            exceptionHandler.handleSecurityException((HttpServletRequest) servletRequest,
                    (HttpServletResponse) servletResponse,
                     ex, HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
