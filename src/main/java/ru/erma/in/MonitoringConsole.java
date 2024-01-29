package ru.erma.in;

import ru.erma.handler.*;
import ru.erma.model.UserRole;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the main console of the application.
 * It manages the user session and delegates actions to the appropriate handlers based on the user's role.
 */
public class MonitoringConsole {
    private final HandlerDependencies dependencies;
    private final Map<UserRole, ActionHandler> handlers;

    /**
     * Constructor for the MonitoringConsole class.
     * Initializes the dependencies and handlers.
     *
     * @param dependencies the dependencies required by the console
     */
    public MonitoringConsole(HandlerDependencies dependencies) {
        this.dependencies = dependencies;

        handlers = new HashMap<>();
        handlers.put(UserRole.GUEST, new GuestActionHandler(dependencies));
        handlers.put(UserRole.USER, new UserActionHandler(dependencies));
        handlers.put(UserRole.ADMIN, new AdminActionHandler(dependencies));
    }

    /**
     * Starts the console.
     * Continuously prompts the user for actions and delegates the actions to the appropriate handlers.
     */
    public void start() {
        while (true) {
            ActionHandler actionHandler;
            if (!dependencies.session().isLoggedIn()) {
                dependencies.mainHandler().displayMainMenu();
                actionHandler = handlers.get(UserRole.GUEST);
            } else if ("admin".equals(dependencies.session().getUsername())) {
                dependencies.adminHandler().displayAdminMenu();
                actionHandler = handlers.get(UserRole.ADMIN);
            } else {
                dependencies.userHandler().displayUserMenu();
                actionHandler = handlers.get(UserRole.USER);
            }
            int choice = dependencies.userHandler().readChoice();
            actionHandler.handleAction(choice);
        }
    }
}