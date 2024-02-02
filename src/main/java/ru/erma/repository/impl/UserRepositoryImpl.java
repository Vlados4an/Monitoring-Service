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

public class UserRepositoryImpl extends AbstractRepository implements UserRepository<String, User> {

    public UserRepositoryImpl(DBConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

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

    @Override
    public void save(User user) {
        String sql = "INSERT INTO develop.users(username,password,salt) VALUES (?,?,?)";
        executeUpdate(sql, user.getUsername(), user.getPassword(), user.getSalt());
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