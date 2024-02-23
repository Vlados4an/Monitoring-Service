package ru.erma.service;

import org.aspectj.lang.JoinPoint;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.erma.dto.SecurityDTO;

@Service
public class UsernameProviderImpl implements UsernameProvider {
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