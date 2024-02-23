package ru.erma.aop.aspects;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.erma.aop.annotations.Audit;
import ru.erma.service.AuditService;
import ru.erma.service.UsernameProvider;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {
    private final AuditService auditService;
    private final UsernameProvider usernameProvider;

    @Pointcut("@annotation(audit)")
    public void measurePointCut(Audit audit) {
    }

    @AfterReturning(value = "@annotation(audit)", argNames = "joinPoint,audit")
    public void methodExecutionMeasure(JoinPoint joinPoint, Audit audit) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();

        String username = usernameProvider.getUsername(joinPoint, methodName);

        String auditInfo = audit.action().isEmpty() ? "Called method " + methodName + " of class " + className : audit.action();

        ru.erma.model.Audit auditRecord = new ru.erma.model.Audit(username, LocalDateTime.now(), auditInfo);
        auditService.saveAudit(auditRecord);
    }
}