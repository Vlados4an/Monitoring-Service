package ru.erma.service;

import org.aspectj.lang.JoinPoint;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.erma.dto.SecurityDTO;

/**
 * This class provides a service for retrieving the username from the security context or method arguments.
 * It implements the UsernameProvider interface.
 * Spring context will autodetect these classes when annotation-based configuration and classpath scanning is used.
 */
@Service
public class UsernameProviderImpl implements UsernameProvider {

    /**
     * This method is used to get the username from the method arguments if the method is "register" or "authorization".
     * If the username is not found in the method arguments or the method is not "register" or "authorization",
     * it gets the username from the security context.
     *
     * @param joinPoint the join point at which the advice is applied
     * @param methodName the name of the method
     * @return the username
     */
    @Override
    public String getUsername(JoinPoint joinPoint, String methodName) {
        String username = "";

        if (methodName.equals("register") || methodName.equals("authorization")) {
            Object[] args = joinPoint.getArgs();
            for (Object arg : args) {
                if (arg instanceof SecurityDTO) {
                    username = ((SecurityDTO) arg).username();
                    break;
                }
            }
        }

        if (username.isEmpty()) {
            username = SecurityContextHolder.getContext().getAuthentication().getName();
        }
        return username;
    }
}