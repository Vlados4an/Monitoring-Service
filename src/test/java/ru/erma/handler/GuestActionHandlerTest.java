package ru.erma.handler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.erma.service.AuditService;
import ru.erma.service.UserService;


import static org.mockito.Mockito.*;
/**
 * This class contains unit tests for the GuestActionHandler class.
 */
@ExtendWith(MockitoExtension.class)
class GuestActionHandlerTest {

    /**
     * Mock of HandlerDependencies used in the tests.
     */
    @Mock
    private HandlerDependencies dependencies;

    /**
     * Mock of UserService used in the tests.
     */
    @Mock
    private UserService userService;

    /**
     * Mock of AuditService used in the tests.
     */
    @Mock
    private AuditService auditService;

    /**
     * Mock of MainHandler used in the tests.
     */
    @Mock
    private MainHandler mainHandler;

    /**
     * The GuestActionHandler instance under test, with mocked dependencies.
     */
    @InjectMocks
    private GuestActionHandler guestActionHandler;

    /**
     * This test verifies that the handleAction method of GuestActionHandler exits the application and logs the action when the choice is 3.
     */
    @Test
    @DisplayName("HandleAction method exits application when choice is 3")
    void handleAction_exitsApplication_whenChoiceIs3() {
        when(dependencies.mainHandler()).thenReturn(mainHandler);
        when(dependencies.auditService()).thenReturn(auditService);
        guestActionHandler.handleAction(3);

        verify(auditService, times(1)).logAction("Application exited");
    }
    /**
     * This test verifies that the handleAction method of GuestActionHandler does nothing when the choice is invalid.
     */
    @Test
    @DisplayName("HandleAction method does nothing when choice is invalid")
    void handleAction_doesNothing_whenChoiceIsInvalid() {
        guestActionHandler.handleAction(4);

        verifyNoInteractions(userService);
        verifyNoInteractions(auditService);
    }
}