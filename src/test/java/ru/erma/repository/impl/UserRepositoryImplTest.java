package ru.erma.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.erma.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class contains unit tests for the UserRepositoryImpl class.
 */
public class UserRepositoryImplTest {
    private UserRepositoryImpl userRepository;

    /**
     * This method is executed before each test.
     * It initializes the userRepository instance.
     */
    @BeforeEach
    public void setUp(){
        userRepository = new UserRepositoryImpl();
    }

    /**
     * This test verifies that the save method of UserRepositoryImpl adds a user to the repository
     * and that the user can be retrieved afterwards.
     */
    @Test
    @DisplayName("Save method adds a user to the repository and the user can be retrieved")
    public void save_addsUserToRepositoryAndCanBeRetrieved() {
        User user = new User();
        user.setUsername("testUser");
        userRepository.save(user);

        Optional<User> retrievedUser = userRepository.findByUsername("testUser");

        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().getUsername()).isEqualTo("testUser");
    }

    /**
     * This test verifies that the findByUsername method of UserRepositoryImpl returns an empty Optional
     * when the user does not exist in the repository.
     */
    @Test
    @DisplayName("FindByUsername method returns an empty Optional if user does not exist")
    public void findByUsername_returnsEmptyOptionalIfUserDoesNotExist() {
        Optional<User> retrievedUser = userRepository.findByUsername("nonExistentUser");

        assertThat(retrievedUser).isNotPresent();
    }
}