package com.mthree.flighttracker.controller;

import org.springframework.stereotype.Component;
import java.security.MessageDigest;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

@Component
public class PasswordEncoder {

    // Salt value to make hashing more secure
    private static final String SALT = "flighttracker-salt-2025";

    // Minimum password length requirement
    private static final int MIN_PASSWORD_LENGTH = 8;

    // Maximum password length requirement
    private static final int MAX_PASSWORD_LENGTH = 32;


    //Encodes a password using SHA-256 hashing with salt,
    // @throws IllegalArgumentException if password is null, empty, or invalid length

    public String encode(String password) {
        validatePassword(password);

        try {
            // Combine password with salt
            String saltedPassword = password + SALT;

            // Create SHA-256 hash
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));

            // Convert to Base64 string
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error encoding password", e);
        }
    }


    //Matches a raw password against an encoded password
    //@throws IllegalArgumentException if either password is null or empty

    public boolean matches(String rawPassword, String encodedPassword) {
        validatePassword(rawPassword);

        if (encodedPassword == null || encodedPassword.isEmpty()) {
            throw new IllegalArgumentException("Encoded password cannot be null or empty");
        }

        try {
            String newEncodedPassword = encode(rawPassword);
            return MessageDigest.isEqual(
                    newEncodedPassword.getBytes(StandardCharsets.UTF_8),
                    encodedPassword.getBytes(StandardCharsets.UTF_8)
            );
        } catch (Exception e) {
            return false;
        }
    }


    //Validates password meets basic requirements
    //@throws IllegalArgumentException if password is invalid

    private void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException(
                    "Password must be at least " + MIN_PASSWORD_LENGTH + " characters long"
            );
        }

        if (password.length() > MAX_PASSWORD_LENGTH) {
            throw new IllegalArgumentException(
                    "Password cannot be longer than " + MAX_PASSWORD_LENGTH + " characters"
            );
        }
    }
}
