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
 * The type Security service.
 */
public class SecurityService {

    private final UserRepository<String, User> userRepository;

    private final JwtTokenProvider tokenProvider;

    public SecurityService(UserRepository<String, User> userRepository, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

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
