package ru.erma.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.erma.exception.UsernameAlreadyExistException;
import ru.erma.model.User;
import ru.erma.repository.UserRepository;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * This class is used to test the UserService class.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    /**
     * Mock of UserRepository used in the tests.
     */
    @Mock
    private UserRepository<String, User> userRepository;

    /**
     * The UserService instance under test, with mocked dependencies.
     */
    @InjectMocks
    private UserService userService;

    /**
     * This test verifies that when the registerUser method is called with a username that does not exist,
     * it saves a new User.
     */
    @Test
    void registerUser_savesNewUser_whenUsernameDoesNotExist() throws NoSuchAlgorithmException {
        userService.registerUser("testUser", "password");

        verify(userRepository, times(1)).save(any(User.class));
    }

    /**
     * This test verifies that when the registerUser method is called with a username that already exists,
     * it throws a UsernameAlreadyExistException.
     */
    @Test
    void registerUser_throwsException_whenUsernameExists() throws NoSuchAlgorithmException {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> userService.registerUser("testUser", "password"))
                .isInstanceOf(UsernameAlreadyExistException.class)
                .hasMessage("Пользователь с таким именем уже существует");
    }

    /**
     * This test verifies that when the authenticateUser method is called with correct credentials,
     * it returns true.
     */
    @Test
    void authenticateUser_returnsTrue_whenCorrectCredentialsProvided() throws NoSuchAlgorithmException {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword(userService.hashPassword("password", "salt"));
        user.setSalt("salt");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        boolean result = userService.authenticateUser("testUser", "password");

        assertThat(result).isTrue();
    }

    /**
     * This test verifies that when the authenticateUser method is called with incorrect credentials,
     * it returns false.
     */
    @Test
    void authenticateUser_returnsFalse_whenIncorrectCredentialsProvided() throws NoSuchAlgorithmException {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword(userService.hashPassword("password", "salt"));
        user.setSalt("salt");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        boolean result = userService.authenticateUser("testUser", "wrongPassword");

        assertThat(result).isFalse();
    }

    /**
     * This test verifies that when the authenticateUser method is called with a username that does not exist,
     * it returns false.
     */
    @Test
    void authenticateUser_returnsFalse_whenUserDoesNotExist() throws NoSuchAlgorithmException {
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        boolean result = userService.authenticateUser("nonExistentUser", "password");

        assertThat(result).isFalse();
    }
}