package ru.erma.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.erma.model.User;
import ru.erma.util.PasswordHasher;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

/**
 * The UserRepositoryImplTest class tests the functionality of the UserRepositoryImpl class.
 * It extends the AbstractRepositoryForTest class to reuse its setup logic.
 */
public class UserRepositoryImplTest extends AbstractRepositoryForTest {
    private UserRepositoryImpl userRepository;

    /**
     * The setUp method initializes the UserRepositoryImpl instance before each test.
     * It calls the setUp method of the superclass to initialize the connection provider,
     * and then creates a new UserRepositoryImpl with the connection provider.
     */
    @BeforeEach
    void setUp() {
        super.setUp();
        userRepository = new UserRepositoryImpl(connectionProvider);
    }

    /**
     * This test checks that the findByUsername method correctly retrieves a user from the database.
     * It asserts that the returned user is present and that its username matches the expected username.
     */
    @Test
    @DisplayName("Test that user is retrieved correctly by username")
    void testFindByUsername() {
        String username = "test_user";

        Optional<User> actualUser = userRepository.findByUsername(username);

        assertThat(actualUser).isPresent();
        assertThat(actualUser.get().getUsername()).isEqualTo(username);
    }

    /**
     * This test checks that the save method correctly saves a user to the database.
     * It saves a user to the database and then retrieves it using the findByUsername method.
     * It asserts that the retrieved user is present and that its username and password match the expected username and password.
     */
    @Test
    @DisplayName("Test that user is saved correctly with all properties")
    void testSaveAllProperties() {
        String username = "testUser228";
        byte[] password = PasswordHasher.hashPassword("testPass");
        User expectedUser = new User();
        expectedUser.setUsername(username);
        expectedUser.setPassword(password);

        userRepository.save(expectedUser);
        Optional<User> actualUser = userRepository.findByUsername(username);

        assertThat(actualUser).isPresent();
        assertThat(actualUser.get().getUsername()).isEqualTo(expectedUser.getUsername());
        assertThat(actualUser.get().getPassword()).isEqualTo(expectedUser.getPassword());
    }

    /**
     * This test checks that the save method throws an exception when trying to save a null user.
     * It asserts that a RuntimeException is thrown when trying to save a null user.
     */
    @Test
    @DisplayName("Test that exception is thrown when saving null user")
    void testSaveNullUser() {
        assertThatThrownBy(() -> userRepository.save(null))
                .isInstanceOf(RuntimeException.class);
    }

    /**
     * This test checks that the findByUsername method correctly handles the case where the user does not exist.
     * It attempts to retrieve a user with a username that does not exist in the database.
     * It asserts that the returned user is not present.
     */
    @Test
    @DisplayName("Test that non-existing user is not retrieved")
    void testFindByUsername_whenUserDoesNotExist() {
        Optional<User> actualUser = userRepository.findByUsername("nonExistingUser");

        assertThat(actualUser).isNotPresent();
    }
}