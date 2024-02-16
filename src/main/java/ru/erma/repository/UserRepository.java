package ru.erma.repository;

import ru.erma.model.Role;

import java.util.Optional;

/**
 * This interface represents a repository for users.
 * It provides methods to find a user by username and to save a user.
 *
 * @param <K> the type of the username
 * @param <E> the type of the user
 */
public interface UserRepository<K,E> {

    /**
     * Finds a user by the specified username.
     *
     * @param username the username of the user to find
     * @return an Optional containing the found user, or an empty Optional if no user was found
     */
    Optional<E> findByUsername(K username);

    Role findRoleByUsername(K username);

    /**
     * Saves the given user.
     *
     * @param user the user to save
     */
    void save(E user);

    Optional<K> findUsername(K username);
}