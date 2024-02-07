package ru.erma.handler;

import lombok.RequiredArgsConstructor;
import ru.erma.model.Audit;

import java.util.List;
import java.util.Scanner;


/**
 * This class represents an admin action handler in the system.
 * An admin action handler provides methods to handle actions based on the admin's choice.
 */
@RequiredArgsConstructor
public class AdminActionHandler implements ActionHandler{
    private final HandlerDependencies dependencies;
    Scanner scanner = new Scanner(System.in);
    /**
     * Handles an admin's action based on their choice.
     * The actions include viewing audits, adding a new reading type, removing an existing reading type,
     * logging out of the account, and exiting the application.
     *
     * @param choice the admin's choice
     */
    @Override
    public void handleAction(int choice) {
        switch (choice) {
            case 1:
                List<Audit> audits = dependencies.auditService().getAllAudits();
                for (Audit audit : audits) {
                    System.out.println(audit);
                }
                dependencies.auditService().logAction("Admin viewed audits");
                break;
            case 2:
                System.out.println("Введите новый тип показаний: ");
                String newType = scanner.nextLine();
                dependencies.readingStructureService().addReadingType(newType);
                dependencies.auditService().logAction("Admin added new reading type: " + newType);
                System.out.println("Новый тип показаний успешно добавлен");
                break;
            case 3:
                System.out.println("Введите тип показаний для удаления: ");
                String removeType = scanner.nextLine();
                boolean removed = dependencies.readingStructureService().removeReadingType(removeType);
                if (removed) {
                    dependencies.auditService().logAction("Admin removed reading type: " + removeType);
                    System.out.println("Тип показаний успешно удален");
                } else {
                    System.out.println("Тип показаний не найден");
                }
                break;
            case 4:
                dependencies.auditService().logAction("Admin logged out");
                dependencies.userHandler().logout();
                dependencies.session().setLoggedIn(false);
                dependencies.session().setUsername(null);
                break;
            case 5:
                dependencies.mainHandler().exitApplication();
                dependencies.auditService().logAction("Application exited");
                break;
            default:
                System.out.println("Неверный выбор. Пожалуйста, выберите снова.");
        }
    }
}