package ru.erma.handler;

/**
 * This class represents the main handler in the system.
 * The main handler provides methods to display the main menu and to exit the application.
 */
public class MainHandler {
    /**
     * Displays the main menu.
     * The menu includes options to register a new user, authenticate a user, and exit the application.
     */
    public void displayMainMenu(){
        System.out.println("╔═════════════════════════════════════════════════╗");
        System.out.println("║         Выберите действие:                      ║");
        System.out.println("║ 1. Регистрация                                  ║");
        System.out.println("║ 2. Авторизация                                  ║");
        System.out.println("║ 3. Выйти из приложения                          ║");
        System.out.println("╚═════════════════════════════════════════════════╝");

    }

    /**
     * Exits the application.
     * Prints a message and terminates the JVM.
     */
    public void exitApplication() {
        System.out.println("Выход из приложения.");
        System.exit(0);
    }
}
