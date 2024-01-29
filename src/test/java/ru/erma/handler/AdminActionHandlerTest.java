package ru.erma.handler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.erma.model.AuditLog;
import ru.erma.model.Session;
import ru.erma.service.AuditService;
import ru.erma.service.ReadingStructureService;

import java.util.Arrays;

import static org.mockito.Mockito.*;

/**
 * This class is used to test the AdminActionHandler class.
 * It uses the Mockito framework for mocking objects and JUnit for running the tests.
 */
@ExtendWith(MockitoExtension.class)
class AdminActionHandlerTest {
    /**
     * Mock of HandlerDependencies used in the tests.
     */
    @Mock
    private HandlerDependencies dependencies;
    /**
     * Mock of AuditService used in the tests.
     */
    @Mock
    private AuditService auditService;
    /**
     * Mock of ReadingStructureService used in the tests.
     */
    @Mock
    private ReadingStructureService readingStructureService;
    /**
     * Mock of UserHandler used in the tests.
     */
    @Mock
    private UserHandler userHandler;
    /**
     * Mock of Session used in the tests.
     */
    @Mock
    private Session session;
    /**
     * Mock of MainHandler used in the tests.
     */
    @Mock
    private MainHandler mainHandler;

    /**
     * The AdminActionHandler instance under test, with mocked dependencies.
     */
    @InjectMocks
    private AdminActionHandler adminActionHandler;

    /**
     * This test verifies that when the handleAction method is called with 1 as argument,
     * it retrieves all audits and logs the action.
     */
    @Test
    void handleAction_displaysAudits_whenChoiceIs1() {
        AuditLog audit1 = new AuditLog();
        audit1.getLogs().add("action1");
        AuditLog audit2 = new AuditLog();
        audit2.getLogs().add("action2");
        when(dependencies.auditService()).thenReturn(auditService);
        when(dependencies.auditService().getAllAudits()).thenReturn(Arrays.asList(audit1, audit2));

        adminActionHandler.handleAction(1);

        verify(dependencies.auditService(), times(1)).getAllAudits();
        verify(dependencies.auditService(), times(1)).logAction("Admin viewed audits");
    }

    /**
     * This test verifies that when the handleAction method is called with 4 as argument,
     * it logs out the user and logs the action.
     */
    @Test
    void handleAction_logsOut_whenChoiceIs4() {
        when(dependencies.session()).thenReturn(session);
        when(dependencies.userHandler()).thenReturn(userHandler);
        when(dependencies.auditService()).thenReturn(auditService);
        adminActionHandler.handleAction(4);

        verify(dependencies.userHandler(), times(1)).logout();
        verify(dependencies.auditService(), times(1)).logAction("Admin logged out");
    }

    /**
     * This test verifies that when the handleAction method is called with 5 as argument,
     * it exits the application and logs the action.
     */
    @Test
    void handleAction_exitsApplication_whenChoiceIs5() {
        when(dependencies.mainHandler()).thenReturn(mainHandler);
        when(dependencies.auditService()).thenReturn(auditService);
        adminActionHandler.handleAction(5);

        verify(dependencies.mainHandler(), times(1)).exitApplication();
        verify(dependencies.auditService(), times(1)).logAction("Application exited");
    }

    /**
     * This test verifies that when the handleAction method is called with an invalid argument,
     * it does nothing.
     */
    @Test
    void handleAction_doesNothing_whenChoiceIsInvalid() {
        when(dependencies.auditService()).thenReturn(auditService);
        when(dependencies.readingStructureService()).thenReturn(readingStructureService);
        when(dependencies.userHandler()).thenReturn(userHandler);

        adminActionHandler.handleAction(6);

        verifyNoInteractions(dependencies.auditService());
        verifyNoInteractions(dependencies.readingStructureService());
        verifyNoInteractions(dependencies.userHandler());
    }
}