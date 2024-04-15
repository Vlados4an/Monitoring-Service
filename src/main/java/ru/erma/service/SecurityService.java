package ru.erma.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.erma.aop.annotations.Audit;
import ru.erma.dto.JwtResponse;
import ru.erma.dto.SecurityDTO;
import ru.erma.exception.AuthorizeException;
import ru.erma.exception.RegisterException;
import ru.erma.exception.UserNotFoundException;
import ru.erma.in.security.JwtTokenProvider;
import ru.erma.mappers.UserMapper;
import ru.erma.model.Role;
import ru.erma.model.UserEntity;
import ru.erma.repository.UserRepository;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

/**
 * The SecurityService class provides services related to user authentication and registration.
 * It uses a UserRepository to perform operations on User data and a JwtTokenProvider to create and validate JWT tokens.
 */
@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    /**
     * Registers a new user with the specified username and password.
     * If a user with the specified username already exists, it throws a RegisterException.
     * After successfully registering the user, it returns the User object.
     *
     * @param securityDTO the SecurityDTO containing the username and password of the new user
     * @return the User object of the registered user
     * @throws RegisterException if a user with the specified username already exists
     */
    @Audit(action = "User registered")
    public UserEntity register(SecurityDTO securityDTO) {
        String username = securityDTO.username();

        if (userRepository.existsByUsername(username)) {
            throw new RegisterException("The user with this username already exists.");
        }

        UserEntity newUser = userMapper.toUserEntity(securityDTO,passwordEncoder);

        userRepository.save(newUser);
        return newUser;
    }

    /**
     * Authenticates a user with the specified username and password.
     * If the user does not exist or the password is incorrect, it throws an AuthorizeException.
     * After successfully authenticating the user, it creates an access token and a refresh token, and returns a JwtResponse with the tokens.
     *
     * @param securityDTO the SecurityDTO containing the username and password of the user
     * @return a JwtResponse with the access token and refresh token
     * @throws AuthorizeException if the user does not exist or the password is incorrect
     */
    @Audit(action = "User authorized")
    public JwtResponse authorization(SecurityDTO securityDTO) {
        String username = securityDTO.username();
        String password = securityDTO.password();

        if(!userRepository.existsByUsername(username)){
            throw new UserNotFoundException("User not found with username: " + username);
        }

        Role role = userRepository.findRoleByUsername(username);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        String accessToken = tokenProvider.createAccessToken(username, role);
        String refreshToken = tokenProvider.createRefreshToken(username,role);

        try {
            tokenProvider.authentication(accessToken);
        } catch (AccessDeniedException e) {
            throw new AuthorizeException("Access denied!.");
        }

        return new JwtResponse(username, accessToken, refreshToken);
    }

    /**
     * Assigns the role of admin to the user with the specified username.
     * The method first retrieves the user from the UserRepository.
     * If the user does not exist, it throws a UserNotFoundException.
     * Then, it sets the user's role to ADMIN and updates the user in the UserRepository.
     *
     * @param username the username of the user to assign the admin role
     * @throws UserNotFoundException if the user does not exist
     */
    @Audit(action = "Assign new admin")
    public void assignAdmin(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        user.setRole(Role.ADMIN.name());
        userRepository.save(user);
    }
}
