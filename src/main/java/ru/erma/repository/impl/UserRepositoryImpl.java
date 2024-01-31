package ru.erma.repository.impl;

import ru.erma.config.DBConnectionProvider;
import ru.erma.model.User;
import ru.erma.repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * This class provides an implementation of the UserRepository interface.
 * It uses a HashMap to store User objects, using their username as the key.
 */
public class UserRepositoryImpl implements UserRepository<String, User> {
    private final DBConnectionProvider connectionProvider;

    public UserRepositoryImpl(DBConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    /**
     * Finds a User object by the specified username.
     *
     * @param username the username of the user to find
     * @return an Optional containing the found User object, or an empty Optional if no user was found
     */
    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(getUserFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Saves the given User object.
     * The User is stored in the users map, using its username as the key.
     *
     * @param user the User object to save
     */
    @Override
    public void save(User user) {
        String sql = "INSERT INTO users(username,password,salt) VALUES (?,?,?)";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getSalt());
            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

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