package ru.erma.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.erma.dto.JwtResponse;
import ru.erma.dto.SecurityDTO;
import ru.erma.exception.AuthorizeException;
import ru.erma.exception.RegisterException;
import ru.erma.in.security.JwtTokenProvider;
import ru.erma.model.User;
import ru.erma.repository.UserRepository;
import ru.erma.util.PasswordHasher;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * The SecurityServiceTest class provides unit tests for the SecurityService class.
 * It uses Mockito to mock the UserRepository and JwtTokenProvider dependencies of SecurityService.
 */
@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {

    /**
     * A mock UserRepository provided by Mockito.
     */
    @Mock
    private UserRepository<String, User> userRepository;

    /**
     * A mock JwtTokenProvider provided by Mockito.
     */
    @Mock
    private JwtTokenProvider tokenProvider;

    /**
     * The SecurityService instance under test.
     * InjectMocks annotation tells Mockito to inject the mocked UserRepository and JwtTokenProvider into this instance.
     */
    @InjectMocks
    private SecurityService securityService;

    /**
     * This test verifies that the register method of SecurityService returns the correct user when the username is not taken.
     * It sets up the mock UserRepository to return an empty Optional when findByUsername is called.
     * It then calls register on the SecurityService instance and asserts that the returned User's username matches the expected username.
     * It also verifies that save was called on the UserRepository mock.
     */
    @Test
    @DisplayName("Register returns user when username is not taken")
    void register_returnsUserWhenUsernameIsNotTaken() {
        SecurityDTO securityDTO = new SecurityDTO("testUser", "testPass");

        when(userRepository.findByUsername(securityDTO.username())).thenReturn(Optional.empty());

        User result = securityService.register(securityDTO);

        assertThat(result.getUsername()).isEqualTo(securityDTO.username());
        verify(userRepository, times(1)).save(any(User.class));
    }

    /**
     * This test verifies that the register method of SecurityService throws a RegisterException when the username is already taken.
     * It sets up the mock UserRepository to return a User when findByUsername is called.
     * It then calls register on the SecurityService instance and asserts that a RegisterException is thrown.
     */
    @Test
    @DisplayName("Register throws exception when username is already taken")
    void register_throwsExceptionWhenUsernameIsAlreadyTaken() {
        SecurityDTO securityDTO = new SecurityDTO("testUser", "testPass");
        User existingUser = new User();
        existingUser.setUsername(securityDTO.username());

        when(userRepository.findByUsername(securityDTO.username())).thenReturn(Optional.of(existingUser));

        assertThatThrownBy(() -> securityService.register(securityDTO))
                .isInstanceOf(RegisterException.class)
                .hasMessage("The user with this username already exists.");
    }

    /**
     * This test verifies that the authorization method of SecurityService returns a JwtResponse when the credentials are valid.
     * It sets up the mock UserRepository to return a User with the expected username and password when findByUsername is called.
     * It then calls authorization on the SecurityService instance and asserts that the returned JwtResponse's username matches the expected username.
     */
    @Test
    @DisplayName("Authorization returns JwtResponse when credentials are valid")
    void authorization_returnsJwtResponseWhenCredentialsAreValid() {
        SecurityDTO securityDTO = new SecurityDTO("testUser", "testPass");
        User existingUser = new User();
        existingUser.setUsername(securityDTO.username());
        existingUser.setPassword(PasswordHasher.hashPassword(securityDTO.password()));

        when(userRepository.findByUsername(securityDTO.username())).thenReturn(Optional.of(existingUser));

        JwtResponse result = securityService.authorization(securityDTO);

        assertThat(result.username()).isEqualTo(securityDTO.username());
    }

    /**
     * This test verifies that the authorization method of SecurityService throws an AuthorizeException when the user does not exist.
     * It sets up the mock UserRepository to return an empty Optional when findByUsername is called.
     * It then calls authorization on the SecurityService instance and asserts that an AuthorizeException is thrown.
     */
    @Test
    @DisplayName("Authorization throws exception when user does not exist")
    void authorization_throwsExceptionWhenUserDoesNotExist() {
        SecurityDTO securityDTO = new SecurityDTO("testUser", "testPass");

        when(userRepository.findByUsername(securityDTO.username())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> securityService.authorization(securityDTO))
                .isInstanceOf(AuthorizeException.class)
                .hasMessage("There is no user with this username in the database.");
    }

    /**
     * This test verifies that the authorization method of SecurityService throws an AuthorizeException when the password is incorrect.
     * It sets up the mock UserRepository to return a User with a different password when findByUsername is called.
     * It then calls authorization on the SecurityService instance and asserts that an AuthorizeException is thrown.
     */
    @Test
    @DisplayName("Authorization throws exception when password is incorrect")
    void authorization_throwsExceptionWhenPasswordIsIncorrect() {
        SecurityDTO securityDTO = new SecurityDTO("testUser", "testPass");
        User existingUser = new User();
        existingUser.setUsername(securityDTO.username());
        existingUser.setPassword(PasswordHasher.hashPassword("wrongPass"));

        when(userRepository.findByUsername(securityDTO.username())).thenReturn(Optional.of(existingUser));

        assertThatThrownBy(() -> securityService.authorization(securityDTO))
                .isInstanceOf(AuthorizeException.class)
                .hasMessage("Incorrect password.");
    }
}