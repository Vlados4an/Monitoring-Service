package ru.erma.in.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ru.erma.config.DBConnectionProvider;
import ru.erma.config.DBMigrationService;
import ru.erma.config.DatabaseConfiguration;
import ru.erma.in.security.JwtTokenProvider;
import ru.erma.model.Audit;
import ru.erma.model.Reading;
import ru.erma.model.User;
import ru.erma.repository.AuditRepository;
import ru.erma.repository.ReadingRepository;
import ru.erma.repository.ReadingTypeRepository;
import ru.erma.repository.UserRepository;
import ru.erma.repository.impl.AuditRepositoryImpl;
import ru.erma.repository.impl.ReadingRepositoryImpl;
import ru.erma.repository.impl.ReadingTypeRepositoryImpl;
import ru.erma.repository.impl.UserRepositoryImpl;
import ru.erma.service.*;
import ru.erma.util.PropertyLoader;

import java.util.Properties;

@WebListener
public class ApplicationContextListener implements ServletContextListener {

    private Properties properties;
    private DBConnectionProvider connectionProvider;
    private PropertyLoader propertyLoader;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final ServletContext servletContext = sce.getServletContext();

        loadProperties(servletContext);
        databaseConfiguration(servletContext);
        serviceContextInit(servletContext);


        ObjectMapper jacksonMapper = new ObjectMapper();
        servletContext.setAttribute("jacksonMapper", jacksonMapper);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }

    private void databaseConfiguration(ServletContext servletContext) {
        DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration(propertyLoader);

        connectionProvider = databaseConfiguration.getConnectionProvider();
        servletContext.setAttribute("connectionProvider", connectionProvider);

        DBMigrationService migrationService = databaseConfiguration.getMigrationService();
        migrationService.migration();
        servletContext.setAttribute("migrationService", migrationService);
    }
    private void serviceContextInit(ServletContext servletContext) {

        UserRepository<String, User> userRepository = new UserRepositoryImpl(connectionProvider);
        ReadingRepository<String, Reading> readingRepository = new ReadingRepositoryImpl(connectionProvider);
        ReadingTypeRepository<String> readingTypeRepository = new ReadingTypeRepositoryImpl(connectionProvider);
        AuditRepository<Audit> auditRepository = new AuditRepositoryImpl(connectionProvider);

        ReadingStructureService readingStructureService = new ReadingStructureService(readingTypeRepository);
        UserService userService = new UserService(userRepository);
        ReadingService readingService = new ReadingService(readingRepository,readingStructureService);
        AuditService auditService = new AuditService(auditRepository);


        JwtTokenProvider tokenProvider = new JwtTokenProvider(
                properties.getProperty("jwt.secret"),
                Long.parseLong(properties.getProperty("jwt.access")),
                Long.parseLong(properties.getProperty("jwt.refresh")),
                userService
        );

        SecurityService securityService = new SecurityService(userRepository, tokenProvider);


        servletContext.setAttribute("tokenProvider", tokenProvider);
        servletContext.setAttribute("securityService", securityService);
        servletContext.setAttribute("userService", userService);
        servletContext.setAttribute("readingStructureService",readingStructureService);
        servletContext.setAttribute("readingService",readingService);
        servletContext.setAttribute("auditService",auditService);
    }

    private void loadProperties(ServletContext servletContext) {
        String filePath = "application.properties";
        propertyLoader = new PropertyLoader(filePath);
        properties = propertyLoader.getProperties();
        servletContext.setAttribute("servletProperties", properties);
    }
}
