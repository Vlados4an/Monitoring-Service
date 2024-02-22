package ru.erma.aop.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * This aspect is used to log execution information for methods annotated with @Loggable.
 * It uses the AspectJ framework to weave additional behavior into the annotated methods.
 */
@Slf4j
@Aspect
@Component
public class LoggableAspect {

    /**
     * This pointcut matches all methods within classes annotated with @Loggable.
     * The matched methods will be advised by the logging advice.
     */
    @Pointcut("within(@ru.erma.aop.annotations.Loggable *) && execution(* *(..))")
    public void annotatedByLoggable() {}

    /**
     * This advice is executed around the execution of methods matched by the annotatedByLoggable pointcut.
     * It logs the method signature and execution time.
     *
     * @param proceedingJoinPoint provides information about the method execution
     * @return the result of the method execution
     * @throws Throwable if an error occurs during the method execution
     */
    @Around("annotatedByLoggable()")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("Calling method " + proceedingJoinPoint.getSignature());
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();
        log.info("Execution of method " + proceedingJoinPoint.getSignature() +
                " finished. Execution time is " + (endTime - startTime) + " ms");
        return result;
    }
}