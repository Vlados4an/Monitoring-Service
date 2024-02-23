package ru.erma.service;

import org.aspectj.lang.JoinPoint;

public interface UsernameProvider {
    String getUsername(JoinPoint joinPoint, String methodName);
}