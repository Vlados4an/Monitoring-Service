package ru.erma.aop.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This aspect is used to log execution information for methods annotated with @Loggable.
 * It uses the AspectJ framework to weave additional behavior into the annotated methods.
 */
@Aspect
public class LoggableAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggableAspect.class);

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
        logger.info("Calling method " + proceedingJoinPoint.getSignature());
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();
        logger.info("Execution of method " + proceedingJoinPoint.getSignature() +
                " finished. Execution time is " + (endTime - startTime) + " ms");
        return result;
    }
}