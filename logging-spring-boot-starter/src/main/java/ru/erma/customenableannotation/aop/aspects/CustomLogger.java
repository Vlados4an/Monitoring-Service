package ru.erma.customenableannotation.aop.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Aspect
@Slf4j
public class CustomLogger {
    @Around("ru.erma.customenableannotation.aop.aspects.EnableCommonPointcuts.isCustomLogging()")
    public Object addLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startDateTime = LocalDateTime.now().format(formatter);
        String inputArgs = Arrays.toString(joinPoint.getArgs());

        log.info("Method " + joinPoint.getSignature() + " started at " + startDateTime
                + " with input args " + inputArgs);
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        String endDateTime = LocalDateTime.now().format(formatter);
        log.info("Finished at " + endDateTime + ". Execution time is " + (endTime - startTime) + " ms");
        return result;
    }
}
