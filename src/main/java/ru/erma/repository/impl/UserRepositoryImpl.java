package ru.erma.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.erma.model.Role;
import ru.erma.model.UserEntity;
import ru.erma.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository<String, UserEntity> {

    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        String sql = "SELECT username, password FROM develop.users WHERE username = ?";
        List<UserEntity> users = jdbcTemplate.query(sql, new Object[]{username}, new UserRowMapper());
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    @Override
    public void save(UserEntity user) {
        String sql = "INSERT INTO develop.users(username,password,role) VALUES (?,?,?)";
        jdbcTemplate.update(sql, user.getUsername(), user.getPassword(),user.getRole());
    }

    @Override
    public void update(UserEntity user) {
        String sql = "UPDATE develop.users SET role = ? WHERE username = ?";
        jdbcTemplate.update(sql, user.getRole(), user.getUsername());
    }

    @Override
    public Optional<String> findUsername(String username) {
        String sql = "SELECT username FROM develop.users WHERE username = ?";
        List<String> usernames = jdbcTemplate.query(sql, new Object[]{username}, (rs, rowNum) -> rs.getString("username"));
        return usernames.isEmpty() ? Optional.empty() : Optional.of(usernames.get(0));
    }

    @Override
    public Role findRoleByUsername(String username) {
        String sql = "SELECT role FROM develop.users WHERE username = ?";
        String role = jdbcTemplate.queryForObject(sql, new Object[]{username}, String.class);
        return Role.valueOf(role);
    }

    private static class UserRowMapper implements RowMapper<UserEntity> {
        @Override
        public UserEntity mapRow(ResultSet resultSet, int i) throws SQLException {
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(resultSet.getString("username"));
            userEntity.setPassword(resultSet.getString("password"));
            return userEntity;
        }
    }
}