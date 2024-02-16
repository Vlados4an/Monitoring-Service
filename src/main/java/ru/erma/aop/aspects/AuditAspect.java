package ru.erma.aop.aspects;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.erma.aop.annotations.Audit;
import ru.erma.dto.SecurityDTO;
import ru.erma.service.AuditService;

import java.time.LocalDateTime;

/**
 * This aspect is used to log audit information for methods annotated with @Audit.
 * It uses the AspectJ framework to weave additional behavior into the annotated methods.
 */
@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {
    private final AuditService auditService;

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

        String username = getUsername(joinPoint, methodName);

        String auditInfo = audit.action().isEmpty() ? "Called method " + methodName + " of class " + className : audit.action();
        ru.erma.model.Audit auditRecord = new ru.erma.model.Audit(username, LocalDateTime.now(), auditInfo);

        auditService.saveAudit(auditRecord);
    }

    private static String getUsername(JoinPoint joinPoint, String methodName) {
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