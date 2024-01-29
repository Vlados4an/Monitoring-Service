package ru.erma.handler;

import lombok.RequiredArgsConstructor;
import ru.erma.model.Reading;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * This class represents a user action handler in the system.
 * A user action handler provides methods to handle actions based on the user's choice.
 */
@RequiredArgsConstructor
public class UserActionHandler implements ActionHandler {
    private final HandlerDependencies dependencies;
    Scanner scanner = new Scanner(System.in);

    /**
     * Handles a user's action based on their choice.
     * The actions include submitting readings and viewing readings for a specific month.
     *
     * @param choice the user's choice
     */
    @Override
    public void handleAction(int choice) {
        String loggedInUsername = dependencies.session().getUsername();
        switch (choice) {
            case 1:
                Reading actualReadings = dependencies.readingService().getActualReadings(loggedInUsername);
                System.out.println(actualReadings);
                dependencies.auditService().logAction("User viewed actual readings: " + loggedInUsername);
                break;
            case 2:
                submitReadings();
                break;
            case 3:
                viewReadingsForMonth();
                break;
            case 4:
                List<Reading> readings = dependencies.readingService().getReadingHistory(loggedInUsername);
                for (Reading reading : readings){
                    System.out.println(reading);
                }
                dependencies.auditService().logAction("User viewed reading history: " + loggedInUsername);
                break;
            case 5:
                dependencies.auditService().logAction("User logged out: " + loggedInUsername);
                dependencies.userHandler().logout();
                dependencies.session().setLoggedIn(false);
                dependencies.session().setUsername(null);
                break;
            case 6:
                dependencies.mainHandler().exitApplication();
                dependencies.auditService().logAction("Application exited");
                break;
            default:
                System.out.println("Неверный выбор. Пожалуйста, выберите снова.");
        }
    }

    /**
     * Prompts the user to enter readings for a specific month and year, and submits the readings.
     * The readings are saved in the ReadingService, and an action log is created in the AuditService.
     */
    private void submitReadings() {
        System.out.println("Enter month: ");
        int month = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter year: ");
        int year = Integer.parseInt(scanner.nextLine());
        Map<String, Integer> values = new HashMap<>();
        for (String type : dependencies.readingStructureService().getReadingTypes()) {
            System.out.println("Enter " + type + " reading: ");
            values.put(type, Integer.parseInt(scanner.nextLine()));
        }
        dependencies.readingService().submitReadings(dependencies.session().getUsername(), month, year, values);
        dependencies.auditService().logAction("User submitted readings: " + dependencies.session().getUsername());
    }

    /**
     * Prompts the user to enter a month and year, and retrieves and displays the readings for that month and year.
     * If no readings are found for the specified month and year, a message is displayed.
     * If readings are found, they are displayed and an action log is created in the AuditService.
     */
    private void viewReadingsForMonth() {
        System.out.println("Введите месяц: ");
        int month = Integer.parseInt(scanner.nextLine());
        System.out.println("Введите год: ");
        int year = Integer.parseInt(scanner.nextLine());
        List<Reading> readingsForMonth = dependencies.readingService().getReadingsForMonth(dependencies.session().getUsername(), month, year);
        if (readingsForMonth.isEmpty()) {
            System.out.println("Показания за указанный месяц не найдены.");
        } else {
            for (Reading reading : readingsForMonth){
                System.out.println(reading);
            }
            dependencies.auditService().logAction("User viewed readings for month: " + dependencies.session().getUsername());
        }
    }
}