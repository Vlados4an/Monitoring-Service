package ru.erma.repository.impl;

import ru.erma.model.User;
import ru.erma.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This class provides an implementation of the UserRepository interface.
 * It uses a HashMap to store User objects, using their username as the key.
 */
public class UserRepositoryImpl implements UserRepository<String, User> {
    private final Map<String, User> users = new HashMap<>();

    /**
     * Finds a User object by the specified username.
     *
     * @param username the username of the user to find
     * @return an Optional containing the found User object, or an empty Optional if no user was found
     */
    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }

    /**
     * Saves the given User object.
     * The User is stored in the users map, using its username as the key.
     *
     * @param user the User object to save
     */
    @Override
    public void save(User user) {
        users.put(user.getUsername(), user);
    }
}