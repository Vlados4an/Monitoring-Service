package ru.erma.aop.init;

import ru.erma.config.DBConnectionProvider;
import ru.erma.config.DatabaseConfiguration;
import ru.erma.repository.impl.AuditRepositoryImpl;
import ru.erma.service.AuditService;
import ru.erma.util.PropertyLoader;

/**
 * This class is used to initialize the AuditService.
 * It loads the application properties, configures the database connection,
 * and creates an instance of the AuditService with a new AuditRepository.
 */
public class AuditServiceInitializer {
    public AuditServiceInitializer() {
    }

    /**
     * Initializes and returns an instance of the AuditService.
     * It loads the application properties, configures the database connection,
     * and creates an instance of the AuditService with a new AuditRepository.
     *
     * @return an instance of the AuditService
     */
    public static AuditService initializeAuditService() {
        String filePath = "application.properties";
        PropertyLoader propertyLoader = new PropertyLoader(filePath);
        DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration(propertyLoader);
        DBConnectionProvider connectionProvider = databaseConfiguration.getConnectionProvider();
        AuditRepositoryImpl auditRepository = new AuditRepositoryImpl(connectionProvider);
        return new AuditService(auditRepository);
    }
}