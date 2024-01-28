package ru.erma.handler;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This class represents a user handler in the system.
 * A user handler provides methods to read user's choice, log out a user, and display the user menu.
 */
public class UserHandler {
    Scanner scanner = new Scanner(System.in);

    /**
     * Reads the user's choice from the input.
     * If the input is not an integer, an InputMismatchException is caught and the method returns 0.
     *
     * @return the user's choice as an integer
     */
    public int readChoice(){
        int choice = 0;
        try{
            choice = scanner.nextInt();
            scanner.nextLine();
        }catch (InputMismatchException e){
            scanner.nextLine();
        }
        return choice;
    }

    /**
     * Logs out a user by printing a logout message.
     */
    public void logout() {
        System.out.println("Выход из аккаунта.");
    }

    /**
     * Displays the user menu.
     * The menu includes options to get actual readings, submit readings, view readings for a specific month,
     * view reading history, log out, and exit the application.
     */
    public void displayUserMenu() {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║       Выберите действие:                       ║");
        System.out.println("║  1. Получение актуальных показаний счётчиков   ║");
        System.out.println("║  2. Подача показаний                           ║");
        System.out.println("║  3. Просмотр показаний за конкретный месяц     ║");
        System.out.println("║  4. Просмотр Истории подачи показаний          ║");
        System.out.println("║  5. Выйти из аккаунта                          ║");
        System.out.println("║  6. Выйти из приложения                        ║");
        System.out.println("╚════════════════════════════════════════════════╝");
    }
}