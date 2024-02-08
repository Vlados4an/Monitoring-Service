package ru.erma.service;

import ru.erma.aop.annotations.Loggable;
import ru.erma.exception.AuthorizeException;
import ru.erma.exception.UserNotFoundException;
import ru.erma.model.User;
import ru.erma.repository.UserRepository;

import java.util.Optional;

/**
 * The UserService class provides services related to User operations.
 * It uses a UserRepository to perform operations on User data.
 * It is annotated with @Loggable, which means that method execution times are logged.
 */
@Loggable
public class UserService {

    private final UserRepository<String, User> userRepository;

    /**
     * Constructs a new UserService instance with the specified UserRepository.
     *
     * @param userRepository the UserRepository used to perform operations on User data
     */
    public UserService(UserRepository<String, User> userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves a user by username.
     * If the user does not exist, it throws a UserNotFoundException.
     * It is annotated with @Loggable, which means that the execution time of this method is logged.
     *
     * @param username the username of the user to be retrieved
     * @throws UserNotFoundException if the user does not exist
     */
    @Loggable
    public void getByUsername(String username) {
        Optional<User> optionalPlayer = userRepository.findByUsername(username);
        optionalPlayer.orElseThrow(() -> new UserNotFoundException("Player with username " + username + " not found!"));
    }
}
