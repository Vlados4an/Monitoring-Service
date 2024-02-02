package ru.erma.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.erma.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class UserRepositoryImplTest extends AbstractRepositoryForTest {
    private UserRepositoryImpl userRepository;

    @BeforeEach
    void setUp() {
        super.setUp();
        userRepository = new UserRepositoryImpl(connectionProvider);
    }

    @Test
    void testFindByUsername() {
        // Arrange
        String username = "test_user";

        // Act
        Optional<User> actualUser = userRepository.findByUsername(username);

        // Assert
        assertThat(actualUser).isPresent();
        assertThat(actualUser.get().getUsername()).isEqualTo(username);
    }
    @Test
    void testSave() {
        // Arrange
        String username = "testUser228";
        User expectedUser = new User();
        expectedUser.setUsername(username);
        expectedUser.setPassword("testPass");
        expectedUser.setSalt("testSalt");

        // Act
        userRepository.save(expectedUser);
        Optional<User> actualUser = userRepository.findByUsername(username);

        // Assert
        assertThat(actualUser).isPresent();
        assertThat(actualUser.get().getUsername()).isEqualTo(expectedUser.getUsername());
    }
    @Test
    void testFindByUsername_whenUserDoesNotExist() {
        // Act
        Optional<User> actualUser = userRepository.findByUsername("nonExistingUser");

        // Assert
        assertThat(actualUser).isNotPresent();
    }
}