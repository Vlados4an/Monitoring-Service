package ru.erma.factory;

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
import ru.erma.repository.UserRepository;
import ru.erma.repository.impl.AuditRepositoryImpl;
import ru.erma.repository.impl.ReadingRepositoryImpl;
import ru.erma.repository.impl.UserRepositoryImpl;
import ru.erma.service.AuditService;
import ru.erma.service.ReadingService;
import ru.erma.service.ReadingStructureService;
import ru.erma.service.UserService;

import java.security.NoSuchAlgorithmException;

/**
 * This class is responsible for creating and initializing the main components of the application.
 */
public class AppFactory {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    /**
     * Creates and initializes the main components of the application.
     *
     * @return a MonitoringConsole instance with all dependencies set
     */
    public static MonitoringConsole createApp() {
        UserRepository<String, User> userRepository = new UserRepositoryImpl();
        ReadingRepository<String, Reading> readingRepository = new ReadingRepositoryImpl();
        AuditRepository<Audit> auditRepository = new AuditRepositoryImpl();

        UserService userService = new UserService(userRepository);
        ReadingService readingService = new ReadingService(readingRepository);
        AuditService auditService = new AuditService(auditRepository);
        ReadingStructureService readingStructureService = new ReadingStructureService();

        registerAdmin(userService);

        MainHandler mainHandler = new MainHandler();
        UserHandler userHandler = new UserHandler();
        AdminHandler adminHandler= new AdminHandler();
        Session session = new Session();

        HandlerDependencies dependencies = new HandlerDependencies(userService, readingService, auditService, readingStructureService, userHandler, mainHandler,adminHandler, session);

        return new MonitoringConsole(dependencies);
    }

    /**
     * Registers the admin user.
     *
     * @param userService the UserService to use for registering the admin
     */
    private static void registerAdmin(UserService userService) {
        try {
            userService.registerUser(ADMIN_USERNAME, ADMIN_PASSWORD);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error initializing hash algorithm", e);
        }
    }
}