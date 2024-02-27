package ru.erma.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.erma.aop.aspects.AuditAspect;
import ru.erma.service.AuditAspectService;
import ru.erma.service.UsernameProvider;

@Configuration
public class AuditAutoConfiguration {

    @Bean
    @ConditionalOnBean({AuditAspectService.class, UsernameProvider.class})
    public AuditAspect auditAspect(AuditAspectService auditAspectService, UsernameProvider usernameProvider) {
        return new AuditAspect(auditAspectService, usernameProvider);
    }
}