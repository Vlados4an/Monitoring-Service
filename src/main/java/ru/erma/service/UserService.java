package ru.erma.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.erma.customenableannotation.aop.annotation.ActionLogger;
import ru.erma.exception.UserNotFoundException;
import ru.erma.model.User;
import ru.erma.model.UserEntity;
import ru.erma.repository.UserRepository;

/**
 * The UserService class provides services related to User operations.
 * It uses a UserRepository to perform operations on User data.
 * It is annotated with @Loggable, which means that method execution times are logged.
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository<String, UserEntity> userRepository;

    /**
     * Loads the user details by the given username.
     * This method is required by the UserDetailsService interface in Spring Security.
     * It is used for authentication and authorization.
     * The method is annotated with @Loggable, which means that its execution time is logged.
     *
     * @param username the username of the user to load
     * @return a User object containing the user's details
     * @throws UsernameNotFoundException if the user is not found
     */
    @ActionLogger
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Player with username " + username + " not found!"));

        return new User(userEntity);
    }
}
