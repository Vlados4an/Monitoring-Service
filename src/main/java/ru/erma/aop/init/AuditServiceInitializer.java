package ru.erma.aop.init;

import ru.erma.config.DBConnectionProvider;
import ru.erma.config.DatabaseConfiguration;
import ru.erma.repository.impl.AuditRepositoryImpl;
import ru.erma.service.AuditService;
import ru.erma.util.PropertyLoader;

public class AuditServiceInitializer {
    public AuditServiceInitializer() {
    }

    public static AuditService initializeAuditService() {
        String filePath = "application.properties";
        PropertyLoader propertyLoader = new PropertyLoader(filePath);
        DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration(propertyLoader);
        DBConnectionProvider connectionProvider = databaseConfiguration.getConnectionProvider();
        AuditRepositoryImpl auditRepository = new AuditRepositoryImpl(connectionProvider);
        return new AuditService(auditRepository);
    }
}