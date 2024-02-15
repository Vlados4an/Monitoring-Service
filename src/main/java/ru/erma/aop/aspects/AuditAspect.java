package ru.erma.aop.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import ru.erma.aop.annotations.Audit;
import ru.erma.aop.init.AuditServiceInitializer;
import ru.erma.service.AuditService;

/**
 * This aspect is used to log audit information for methods annotated with @Audit.
 * It uses the AspectJ framework to weave additional behavior into the annotated methods.
 */
@Aspect
public class AuditAspect {

    /**
     * This pointcut matches all methods annotated with @Audit.
     * The matched methods will be advised by the methodExecutionMeasure advice.
     *
     * @param audit the @Audit annotation
     */
    @Pointcut("@annotation(audit)")
    public void measurePointCut(Audit audit) {}

    /**
     * This advice is executed after the return of methods matched by the measurePointCut pointcut.
     * It logs the audit information specified by the @Audit annotation.
     *
     * @param joinPoint provides information about the method execution
     * @param audit the @Audit annotation
     */
    @AfterReturning(value = "measurePointCut(audit)", argNames = "joinPoint,audit")
    public void methodExecutionMeasure(JoinPoint joinPoint, Audit audit) {
        String methodName = joinPoint.getSignature().getName();

        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();

        String auditInfo = audit.action().isEmpty() ? "Called method " + methodName + " of class " + className : audit.action();

        AuditService auditService = AuditServiceInitializer.initializeAuditService();

        auditService.logAction(auditInfo);
    }
}