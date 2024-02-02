package ru.erma.repository.impl;

import ru.erma.config.DBConnectionProvider;
import ru.erma.exception.DatabaseException;
import ru.erma.model.User;
import ru.erma.repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
/**
 * The UserRepositoryImpl class provides an implementation of the UserRepository interface.
 * It provides methods to save and retrieve user records from the database.
 */
public class UserRepositoryImpl extends AbstractRepository implements UserRepository<String, User> {
    /**
     * Constructs a new UserRepositoryImpl with the specified connection provider.
     *
     * @param connectionProvider the provider for database connections.
     */
    public UserRepositoryImpl(DBConnectionProvider connectionProvider) {
        super(connectionProvider);
    }
    /**
     * Retrieves a user record for a specific username from the database.
     * It selects the row from the users table where the username matches the provided username.
     * If there is an error retrieving the user record, it throws a DatabaseException.
     *
     * @param username the username to find the user record for.
     * @return an Optional containing the user record if found, or an empty Optional if not found.
     * @throws DatabaseException if there is an error retrieving the user record.
     */

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM develop.users WHERE username = ?";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(getUserFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find user by username", e);
        }
        return Optional.empty();
    }
    /**
     * Saves a user record to the database.
     * It inserts a new row into the users table with the username, password, and salt from the user record.
     * If the user record is null, it throws a DatabaseException.
     *
     * @param user the user record to save.
     * @throws DatabaseException if the user record is null.
     */

    @Override
    public void save(User user) {
        String sql = "INSERT INTO develop.users(username,password,salt) VALUES (?,?,?)";
        executeUpdate(sql, user.getUsername(), user.getPassword(), user.getSalt());
    }

    /**
     * Creates a User instance from a row in the result set.
     * It gets the username, password, and salt from the result set and sets them in the User instance.
     *
     * @param resultSet the result set.
     * @return a User instance with the username, password, and salt from the result set.
     * @throws SQLException if there is an error getting the username, password, or salt from the result set.
     */

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        String salt = resultSet.getString("salt");

        user.setUsername(username);
        user.setPassword(password);
        user.setSalt(salt);
        return user;
    }
}