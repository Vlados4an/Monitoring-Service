package ru.erma.handler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.erma.model.Session;
import ru.erma.service.AuditService;

import static org.mockito.Mockito.*;
/**
 * This class contains unit tests for the UserActionHandler class.
 */
@ExtendWith(MockitoExtension.class)
class UserActionHandlerTest {

    /**
     * Mock of HandlerDependencies used in the tests.
     */
    @Mock
    private HandlerDependencies dependencies;

    /**
     * Mock of UserHandler used in the tests.
     */
    @Mock
    private UserHandler userHandler;

    /**
     * Mock of AuditService used in the tests.
     */
    @Mock
    private AuditService auditService;

    /**
     * Mock of Session used in the tests.
     */
    @Mock
    private Session session;

    /**
     * The UserActionHandler instance under test, with mocked dependencies.
     */
    @InjectMocks
    private UserActionHandler userActionHandler;

    /**
     * This test verifies that the handleAction method of UserActionHandler logs out the user and logs the action when the choice is 5.
     */

    @Test
    @DisplayName("HandleAction method logs out when choice is 5")
    void handleAction_logsOut_whenChoiceIs5() {
        when(dependencies.session()).thenReturn(session);
        when(dependencies.userHandler()).thenReturn(userHandler);
        when(dependencies.auditService()).thenReturn(auditService);
        userActionHandler.handleAction(5);

        verify(dependencies.userHandler(), times(1)).logout();
        verify(dependencies.auditService(), times(1)).logAction("User logged out: null");
    }
}