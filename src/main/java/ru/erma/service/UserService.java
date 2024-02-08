package ru.erma.service;

import ru.erma.aop.annotations.Loggable;
import ru.erma.exception.UserNotFoundException;
import ru.erma.model.User;
import ru.erma.repository.UserRepository;

import java.util.Optional;

/**
 * The type Player service.
 */

@Loggable
public class UserService {

    private final UserRepository<String, User> userRepository;

    public UserService(UserRepository<String, User> userRepository) {
        this.userRepository = userRepository;
    }

    @Loggable
    public void getByUsername(String username) {
        Optional<User> optionalPlayer = userRepository.findByUsername(username);
        optionalPlayer.orElseThrow(() -> new UserNotFoundException("Player with username " + username + " not found!"));
    }
}
