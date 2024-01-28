package ru.erma.handler;

/**
 * This class represents an admin handler in the system.
 * An admin handler provides a method to display the admin menu.
 */
public class AdminHandler {
    /**
     * Displays the admin menu.
     * The menu includes options to view audits, add a new reading type, remove an existing reading type,
     * log out of the account, and exit the application.
     */
    public void displayAdminMenu() {
        System.out.println("╔═════════════════════════════════════════════════════════════╗");
        System.out.println("║ Привет, Админ. Рад видеть вас!                              ║");
        System.out.println("║ Выберите действие:                                          ║");
        System.out.println("║ 1. Просмотр аудитов                                         ║");
        System.out.println("║ 2. Добавить новый тип показаний                             ║");
        System.out.println("║ 3. Удалить существующий тип показаний                       ║");
        System.out.println("║ 4. Выйти из аккаунта                                        ║");
        System.out.println("║ 5. Выйти из приложения                                      ║");
        System.out.println("╚═════════════════════════════════════════════════════════════╝");
    }
}