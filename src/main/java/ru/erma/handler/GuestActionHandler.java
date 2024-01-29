package ru.erma.handler;

import lombok.RequiredArgsConstructor;

import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * This class represents a guest action handler in the system.
 * A guest action handler provides methods to handle actions based on the guest's choice.
 */
@RequiredArgsConstructor
public class GuestActionHandler implements ActionHandler {
    private final HandlerDependencies dependencies;
    Scanner scanner = new Scanner(System.in);

    /**
     * Handles a guest's action based on their choice.
     * The actions include registering a new user, authenticating a user, and exiting the application.
     *
     * @param choice the guest's choice
     */
    @Override
    public void handleAction(int choice) {
        switch (choice) {
            case 1:
                System.out.print("Введите имя пользователя: ");
                String username = scanner.nextLine();
                System.out.print("Введите пароль: ");
                String password = scanner.nextLine();
                try {
                    dependencies.userService().registerUser(username, password);
                } catch (NoSuchAlgorithmException e) {
                    System.out.println("Произошла ошибка при регистрации пользователя. Пожалуйста, попробуйте еще раз.");
                }
                System.out.println("Привет, " + username + ". Вы успешно зарегистрировались.");
                dependencies.auditService().logAction("User successfully registered: " + username);
                break;
            case 2:
                System.out.print("Введите имя пользователя: ");
                String authenticateUsername = scanner.nextLine();
                System.out.print("Введите пароль: ");
                String authenticatePassword = scanner.nextLine();
                boolean isAuthenticated = false;
                try {
                    isAuthenticated = dependencies.userService().authenticateUser(authenticateUsername, authenticatePassword);
                } catch (NoSuchAlgorithmException e) {
                    System.out.println("Произошла ошибка при регистрации пользователя. Пожалуйста, попробуйте еще раз.");
                }
                if (isAuthenticated) {
                    dependencies.session().setLoggedIn(true);
                    dependencies.session().setUsername(authenticateUsername);
                    System.out.println("Пользователь успешно авторизован: " + authenticateUsername);
                    dependencies.auditService().logAction("User successfully authorized: " + authenticateUsername);
                } else {
                    System.out.println("Ошибка авторизации. Пожалуйста, проверьте имя пользователя и пароль.");
                }
                break;
            case 3:
                dependencies.mainHandler().exitApplication();
                dependencies.auditService().logAction("Application exited");
                break;
            default:
                System.out.println("Неверный выбор. Пожалуйста, выберите снова.");
        }
    }
}