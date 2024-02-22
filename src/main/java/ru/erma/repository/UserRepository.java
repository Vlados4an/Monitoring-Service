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

    /**
     * Finds a role by the specified username.
     *
     * @param username the username of the user whose role to find
     * @return the role of the user with the specified username
     */
    Role findRoleByUsername(K username);

    /**
     * Saves the given user.
     *
     * @param user the user to save
     */
    void save(E user);

    /**
     * Finds a username by the specified username.
     *
     * @param username the username to find
     * @return an Optional containing the found username, or an empty Optional if no username was found
     */
    Optional<K> findUsername(K username);

    /**
     * Updates the given user.
     *
     * @param user the user to update
     */
    void update(E user);
}