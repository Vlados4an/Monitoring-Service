package ru.erma.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.erma.exception.UserNotFoundException;
import ru.erma.model.UserEntity;
import ru.erma.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * The UserServiceTest class provides unit tests for the UserService class.
 * It uses Mockito to mock the UserRepository dependency of UserService.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    /**
     * Mock of UserRepository used in the tests.
     */
    @Mock
    private UserRepository<String, UserEntity> userRepository;

    /**
     * The UserService instance under test, with mocked dependencies.
     */
    @InjectMocks
    private UserService userService;

    /**
     * This test verifies that the getByUsername method of UserService returns the correct user when the user exists.
     * It sets up the mock UserRepository to return a specific user when findByUsername is called.
     * It then calls getByUsername on the UserService instance and verifies that findByUsername on the UserRepository mock was called.
     */
    @Test
    @DisplayName("GetByUsername returns user when user exists")
    void getByUsername_returnsUserWhenUserExists() {
        String username = "testUser";
        UserEntity expectedUser = new UserEntity();
        expectedUser.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(expectedUser));

        userService.loadUserByUsername(username);

        verify(userRepository, times(1)).findByUsername(username);
    }

    /**
     * This test verifies that the getByUsername method of UserService throws a UserNotFoundException when the user does not exist.
     * It sets up the mock UserRepository to return an empty Optional when findByUsername is called.
     * It then calls getByUsername on the UserService instance and asserts that a UserNotFoundException is thrown.
     */
    @Test
    @DisplayName("GetByUsername throws exception when user does not exist")
    void getByUsername_throwsExceptionWhenUserDoesNotExist() {
        String username = "testUser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.loadUserByUsername(username))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("Player with username " + username + " not found!");
    }

}