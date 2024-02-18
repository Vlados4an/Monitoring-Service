package ru.erma.repository.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.erma.model.Role;
import ru.erma.model.UserEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static ru.erma.model.Role.ADMIN;
import static ru.erma.model.Role.USER;

/**
 * The UserRepositoryImplTest class tests the functionality of the UserRepositoryImpl class.
 * It extends the AbstractRepositoryForTest class to reuse its setup logic.
 */
public class UserRepositoryImplTest extends AbstractRepositoryForTest {
    private UserRepositoryImpl userRepository;

    /**
     * Sets up the UserRepositoryImpl instance before each test.
     * Calls the setUp method of the superclass to initialize the JdbcTemplate,
     * and then creates a new AuditRepositoryImpl with the JdbcTemplate.
     */
    @BeforeEach
    void setUp() {
        super.setUp();
        userRepository = new UserRepositoryImpl(jdbcTemplate);
    }

    /**
     * This test checks that the findByUsername method correctly retrieves a user from the database.
     * It asserts that the returned user is present and that its username matches the expected username.
     */
    @Test
    @DisplayName("User is retrieved correctly by username")
    void shouldFindByUsername() {
        String username = "test_user";

        Optional<UserEntity> actualUser = userRepository.findByUsername(username);

        assertThat(actualUser).isPresent();
        assertThat(actualUser.get().getUsername()).isEqualTo(username);
    }

    /**
     * This test checks that the save method correctly saves a user to the database.
     * It saves a user to the database and then retrieves it using the findByUsername method.
     * It asserts that the retrieved user is present and that its username and password match the expected username and password.
     */
    @Test
    @DisplayName("User is saved correctly")
    void shouldSaveUser() {
        UserEntity user = new UserEntity();
        user.setUsername("test");
        user.setPassword("testPass");
        user.setRole(USER.name());

        assertThatCode(() -> userRepository.save(user)).doesNotThrowAnyException();

        Optional<UserEntity> savedUser = userRepository.findByUsername(user.getUsername());

        assertThat(savedUser).isPresent();
        assertThat(savedUser.get().getUsername()).isEqualTo(user.getUsername());
        assertThat(savedUser.get().getPassword()).isEqualTo(user.getPassword());
    }

    /**
     * This test checks that the save method throws an exception when trying to save a null user.
     * It asserts that a RuntimeException is thrown when trying to save a null user.
     */
    @Test
    @DisplayName("Exception is thrown when saving null user")
    void shouldThrowExceptionWhenSavingNullUser() {
        Assertions.assertThatThrownBy(() -> userRepository.save(null))
                .isInstanceOf(RuntimeException.class);
    }

    /**
     * Tests that the findRoleByUsername method correctly retrieves the role of a user given their username.
     * Asserts that the returned role matches the expected role.
     */
    @Test
    @DisplayName("Role is retrieved correctly by username")
    void shouldFindRoleByUsername() {
        String username = "test_user";
        Role role = userRepository.findRoleByUsername(username);

        assertThat(role).isEqualTo(USER);
    }

    /**
     * Tests that the update method correctly updates a user's details in the repository.
     * It creates a new UserEntity, saves it to the repository, updates its role, and then updates it in the repository.
     * It then retrieves the updated role from the repository and asserts that it matches the expected role.
     */
    @Test
    @DisplayName("User is updated correctly")
    void shouldUpdateUser() {
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        user.setPassword("testPass");
        user.setRole(USER.name());

        userRepository.save(user);

        user.setRole(ADMIN.name());
        userRepository.update(user);

        Role updatedRole = userRepository.findRoleByUsername(user.getUsername());

        assertThat(updatedRole).isEqualTo(ADMIN);
    }
}