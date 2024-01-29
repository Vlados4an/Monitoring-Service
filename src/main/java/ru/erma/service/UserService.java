package ru.erma.service;

import lombok.RequiredArgsConstructor;
import ru.erma.model.User;
import ru.erma.repository.UserRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * This class provides services related to User operations.
 * It uses a UserRepository to perform operations on User data.
 */
@RequiredArgsConstructor
public class UserService {

    private final UserRepository<String, User> userRepository;

    /**
     * Registers a new user with the given username and password.
     * The password is hashed using a generated salt.
     *
     * @param username the username of the new user
     * @param password the password of the new user
     * @throws NoSuchAlgorithmException if the SHA-256 algorithm is not available
     */
    public void registerUser(String username, String password) throws NoSuchAlgorithmException {
        String salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);
        userRepository.save(new User(username, hashedPassword, salt));
    }

    /**
     * Authenticates a user with the given username and password.
     * The password is hashed and compared with the stored hashed password.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return true if the user is authenticated, false otherwise
     * @throws NoSuchAlgorithmException if the SHA-256 algorithm is not available
     */
    public boolean authenticateUser(String username, String password) throws NoSuchAlgorithmException {
        User user = userRepository.findByUsername(username).orElse(null);
        if(user == null) {
            return false;
        }
        String hashedPassword = hashPassword(password, user.getSalt());
        return hashedPassword.equals(user.getPassword());
    }

    /**
     * Generates a salt for password hashing.
     *
     * @return the generated salt
     */
    private String generateSalt(){
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Hashes a password using the given salt.
     *
     * @param password the password to hash
     * @param salt the salt to use for hashing
     * @return the hashed password
     * @throws NoSuchAlgorithmException if the SHA-256 algorithm is not available
     */
     String hashPassword(String password, String salt) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(salt.getBytes());
        byte[] hashedPassword = messageDigest.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedPassword);
    }
}