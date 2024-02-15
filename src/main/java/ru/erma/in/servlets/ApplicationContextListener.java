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

/**
 * The ApplicationContextListener class implements the ServletContextListener interface and is used to initialize the application context.
 * It is annotated with @WebListener, which means it is automatically registered and applied to every request in the application.
 * It contains methods to handle context initialization and destruction, as well as helper methods to load properties, configure the database, and initialize the service context.
 */
@WebListener
public class ApplicationContextListener implements ServletContextListener {

    private Properties properties;
    private DBConnectionProvider connectionProvider;
    private PropertyLoader propertyLoader;

    /**
     * This method is called by the servlet container to indicate to this servlet that the servlet is being placed into service.
     * It initializes the ObjectMapper, ReadingService, and AuditService from the ServletContext.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final ServletContext servletContext = sce.getServletContext();

        loadProperties(servletContext);
        databaseConfiguration(servletContext);
        serviceContextInit(servletContext);


        ObjectMapper jacksonMapper = new ObjectMapper();
        servletContext.setAttribute("jacksonMapper", jacksonMapper);
    }

    /**
     * This method is called by the servlet container to indicate to a filter that it is being taken out of service.
     * This method is only called once all threads within the filter's doFilter method have exited or after a timeout period has passed.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }

    /**
     * This helper method is used to configure the database.
     * It initializes the DatabaseConfiguration, DBConnectionProvider, and DBMigrationService, and performs database migration.
     *
     * @param servletContext the ServletContext object that contains the servlet context
     */
    private void databaseConfiguration(ServletContext servletContext) {
        DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration(propertyLoader);

        connectionProvider = databaseConfiguration.getConnectionProvider();
        servletContext.setAttribute("connectionProvider", connectionProvider);

        DBMigrationService migrationService = databaseConfiguration.getMigrationService();
        migrationService.migration();
        servletContext.setAttribute("migrationService", migrationService);
    }

    /**
     * This helper method is used to initialize the service context.
     * It initializes the UserRepository, ReadingRepository, ReadingTypeRepository, AuditRepository, ReadingStructureService, UserService, ReadingService, AuditService, JwtTokenProvider, and SecurityService, and sets them in the ServletContext.
     *
     * @param servletContext the ServletContext object that contains the servlet context
     */
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

    /**
     * This helper method is used to load properties.
     * It initializes the PropertyLoader, loads the properties, and sets them in the ServletContext.
     *
     * @param servletContext the ServletContext object that contains the servlet context
     */
    private void loadProperties(ServletContext servletContext) {
        String filePath = "application.properties";
        propertyLoader = new PropertyLoader(filePath);
        properties = propertyLoader.getProperties();
        servletContext.setAttribute("servletProperties", properties);
    }
}
