package ru.erma.factory;

import ru.erma.config.DBConnectionProvider;
import ru.erma.config.DatabaseConfiguration;
import ru.erma.handler.AdminHandler;
import ru.erma.handler.HandlerDependencies;
import ru.erma.handler.MainHandler;
import ru.erma.handler.UserHandler;
import ru.erma.in.MonitoringConsole;
import ru.erma.model.Audit;
import ru.erma.model.Reading;
import ru.erma.model.Session;
import ru.erma.model.User;
import ru.erma.repository.AuditRepository;
import ru.erma.repository.ReadingRepository;
import ru.erma.repository.ReadingTypeRepository;
import ru.erma.repository.UserRepository;
import ru.erma.repository.impl.AuditRepositoryImpl;
import ru.erma.repository.impl.ReadingRepositoryImpl;
import ru.erma.repository.impl.ReadingTypeRepositoryImpl;
import ru.erma.repository.impl.UserRepositoryImpl;
import ru.erma.service.AuditService;
import ru.erma.service.ReadingService;
import ru.erma.service.ReadingStructureService;
import ru.erma.service.UserService;

/**
 * The AppFactory class is responsible for creating and configuring the main application components.
 * It loads the database configuration properties, performs a database migration, and creates the repositories, services, handlers, and session.
 * It then creates a MonitoringConsole instance with the configured dependencies.
 */
public class AppFactory {

    /**
     * Creates and configures the main application components, and returns a MonitoringConsole instance.
     *
     * @return a MonitoringConsole instance with the configured dependencies.
     */
    public static MonitoringConsole createApp() {
        DatabaseConfiguration.loadProperties();
        DBConnectionProvider connectionProvider = DatabaseConfiguration.connectionProviderConfiguration();
        DatabaseConfiguration.databaseMigration();

        UserRepository<String, User> userRepository = new UserRepositoryImpl(connectionProvider);
        ReadingRepository<String, Reading> readingRepository = new ReadingRepositoryImpl(connectionProvider);
        AuditRepository<Audit> auditRepository = new AuditRepositoryImpl(connectionProvider);
        ReadingTypeRepository<String> readingTypeRepository = new ReadingTypeRepositoryImpl(connectionProvider);

        UserService userService = new UserService(userRepository);
        ReadingStructureService readingStructureService = new ReadingStructureService(readingTypeRepository);
        ReadingService readingService = new ReadingService(readingRepository,readingStructureService);
        AuditService auditService = new AuditService(auditRepository);

        MainHandler mainHandler = new MainHandler();
        UserHandler userHandler = new UserHandler();
        AdminHandler adminHandler= new AdminHandler();
        Session session = new Session();

        HandlerDependencies dependencies = new HandlerDependencies(userService, readingService, auditService, readingStructureService, userHandler, mainHandler,adminHandler, session);

        return new MonitoringConsole(dependencies);
    }
}