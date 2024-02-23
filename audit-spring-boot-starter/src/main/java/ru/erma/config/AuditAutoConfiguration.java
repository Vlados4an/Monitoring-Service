package ru.erma.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.erma.aop.aspects.AuditAspect;
import ru.erma.service.AuditService;
import ru.erma.service.UsernameProvider;

@Configuration
public class AuditAutoConfiguration {

    @Bean
    @ConditionalOnBean({AuditService.class, UsernameProvider.class})
    public AuditAspect auditAspect(AuditService auditService, UsernameProvider usernameProvider) {
        return new AuditAspect(auditService, usernameProvider);
    }
}