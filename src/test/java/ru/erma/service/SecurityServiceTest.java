package ru.erma.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.erma.dto.JwtResponse;
import ru.erma.dto.SecurityDTO;
import ru.erma.exception.RegisterException;
import ru.erma.exception.UserNotFoundException;
import ru.erma.in.security.JwtTokenProvider;
import ru.erma.mappers.UserMapper;
import ru.erma.model.Role;
import ru.erma.model.UserEntity;
import ru.erma.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

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
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    /**
     * A mock JwtTokenProvider provided by Mockito.
     */
    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

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
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        userEntity.setPassword("testPass");
        userEntity.setRole(Role.USER.name());

        when(userMapper.toUserEntity(securityDTO,passwordEncoder)).thenReturn(userEntity);

        UserEntity result = securityService.register(securityDTO);

        assertThat(result.getUsername()).isEqualTo(securityDTO.username());
    }

    /**
     * This test verifies that the register method of SecurityService throws a RegisterException when the username is already taken.
     * It sets up the mock UserRepository to return a User when findByUsername is called.
     * It then calls register on the SecurityService instance and asserts that a RegisterException is thrown.
     */
    @Test
    @DisplayName("Register throws exception when username is already taken")
    void register_throwsExceptionWhenUsernameIsAlreadyTaken() {
        String username = "testUser";
        SecurityDTO securityDTO = new SecurityDTO(username, "testPass");


        when(userRepository.existsByUsername(username)).thenReturn(true);

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
        String username = "testUser";
        String password = "testPass";
        SecurityDTO securityDTO = new SecurityDTO(username, password);

        when(userRepository.existsByUsername(username)).thenReturn(true);
        when(userRepository.findRoleByUsername(username)).thenReturn(Role.USER);

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

        assertThatThrownBy(() -> securityService.authorization(securityDTO))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found with username: " + securityDTO.username());
    }

    /**
     * This test verifies that the assignAdmin method correctly assigns a user as an admin.
     * It sets up the mock UserRepository to return a User when findByUsername is called.
     * It then calls assignAdmin on the SecurityService instance and asserts that the User's role is ADMIN.
     * It also verifies that update was called on the UserRepository mock.
     */
    @Test
    @DisplayName("AssignAdmin assigns user as admin successfully")
    void assignAdmin_assignsUserAsAdminSuccessfully() {
        String username = "testUser";
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setRole(Role.USER.name());

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        securityService.assignAdmin(username);

        assertThat(userEntity.getRole()).isEqualTo(Role.ADMIN.name());
        verify(userRepository, times(1)).save(userEntity);
    }

    /**
     * This test verifies that the assignAdmin method throws a UserNotFoundException when the user does not exist.
     * It sets up the mock UserRepository to return an empty Optional when findByUsername is called.
     * It then calls assignAdmin on the SecurityService instance and asserts that a UserNotFoundException is thrown.
     */
    @Test
    @DisplayName("AssignAdmin throws UserNotFoundException when user not found")
    void assignAdmin_throwsUserNotFoundException() {
        String username = "nonExistingUsername";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThatThrownBy(()-> securityService.assignAdmin(username))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found with username: " + username);
    }
}