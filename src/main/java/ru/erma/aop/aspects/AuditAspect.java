package ru.erma.aop.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import ru.erma.aop.annotations.Audit;
import ru.erma.aop.init.AuditServiceInitializer;
import ru.erma.service.AuditService;


@Aspect
public class AuditAspect {
    @Pointcut("@annotation(audit)")
    public void measurePointCut(Audit audit) {
        // This method is a placeholder for the pointcut expression
    }

    @AfterReturning(value = "measurePointCut(audit)", argNames = "joinPoint,audit")
    public void methodExecutionMeasure(JoinPoint joinPoint, Audit audit) {
        String methodName = joinPoint.getSignature().getName();

        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();

        String auditInfo = audit.action().isEmpty() ? "Called method " + methodName + " of class " + className : audit.action();

        AuditService auditService = AuditServiceInitializer.initializeAuditService();

        auditService.logAction(auditInfo);
    }
}