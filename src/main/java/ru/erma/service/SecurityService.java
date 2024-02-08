package ru.erma.service;

import ru.erma.aop.annotations.Audit;
import ru.erma.exception.AuthorizeException;
import ru.erma.exception.RegisterException;
import ru.erma.dto.JwtResponse;
import ru.erma.in.security.JwtTokenProvider;
import ru.erma.model.User;
import ru.erma.repository.UserRepository;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

/**
 * The SecurityService class provides services related to user authentication and registration.
 * It uses a UserRepository to perform operations on User data and a JwtTokenProvider to create and validate JWT tokens.
 */
public class SecurityService {

    private final UserRepository<String, User> userRepository;

    private final JwtTokenProvider tokenProvider;

    /**
     * Constructs a new SecurityService instance with the specified UserRepository and JwtTokenProvider.
     *
     * @param userRepository the UserRepository used to perform operations on User data
     * @param tokenProvider the JwtTokenProvider used to create and validate JWT tokens
     */
    public SecurityService(UserRepository<String, User> userRepository, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    /**
     * Registers a new user with the specified username and password.
     * If a user with the specified username already exists, it throws a RegisterException.
     * After successfully registering the user, it returns the User object.
     *
     * @param username the username of the new user
     * @param password the password of the new user
     * @return the User object of the registered user
     * @throws RegisterException if a user with the specified username already exists
     */
    @Audit(action = "User registered: ")
    public User register(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            throw new RegisterException("The user with this username already exists.");
        }

        User newPlayer = new User();
        newPlayer.setUsername(username);
        newPlayer.setPassword(password);

        userRepository.save(newPlayer);
        return newPlayer;
    }

    /**
     * Authenticates a user with the specified username and password.
     * If the user does not exist or the password is incorrect, it throws an AuthorizeException.
     * After successfully authenticating the user, it creates an access token and a refresh token, and returns a JwtResponse with the tokens.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return a JwtResponse with the access token and refresh token
     * @throws AuthorizeException if the user does not exist or the password is incorrect
     */
    @Audit(action = "User authorized: ")
    public JwtResponse authorization(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new AuthorizeException("There is no player with this username in the database.");
        }

        User player = optionalUser.get();
        if (!player.getPassword().equals(password)) {
            throw new AuthorizeException("Incorrect password.");
        }

        String accessToken = tokenProvider.createAccessToken(username);
        String refreshToken = tokenProvider.createRefreshToken(username);
        try {
            tokenProvider.authentication(accessToken);
        } catch (AccessDeniedException e) {
            throw new AuthorizeException("Access denied!.");
        }
        return new JwtResponse(username, accessToken, refreshToken);
    }
}
