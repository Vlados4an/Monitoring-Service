package ru.erma.handler;

import ru.erma.model.Session;
import ru.erma.service.AuditService;
import ru.erma.service.ReadingService;
import ru.erma.service.ReadingStructureService;
import ru.erma.service.UserService;

/**
 * This class represents the dependencies required by the handlers in the system.
 * It includes services for user, reading, audit, and reading structure operations,
 * handlers for user, main, and admin operations, and a session object.
 */
public record HandlerDependencies(UserService userService, ReadingService readingService,
                                  AuditService auditService, ReadingStructureService readingStructureService,
                                  UserHandler userHandler,
                                  MainHandler mainHandler,AdminHandler adminHandler, Session session) {
}
