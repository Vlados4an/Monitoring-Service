package ru.erma.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.erma.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
     * It asserts that the retrieved user is present and that its username matches the expected username.
     */
    @Test
    @DisplayName("Test that user is saved correctly")
    void testSave() {
        String username = "testUser228";
        User expectedUser = new User();
        expectedUser.setUsername(username);
        expectedUser.setPassword("testPass");
        expectedUser.setSalt("testSalt");

        userRepository.save(expectedUser);
        Optional<User> actualUser = userRepository.findByUsername(username);

        assertThat(actualUser).isPresent();
        assertThat(actualUser.get().getUsername()).isEqualTo(expectedUser.getUsername());
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