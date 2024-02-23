package ru.erma.service;

import ru.erma.aop.annotations.Audit;
import org.aspectj.lang.JoinPoint;

public interface UsernameProvider {
    String getUsername(JoinPoint joinPoint, String methodName);
}