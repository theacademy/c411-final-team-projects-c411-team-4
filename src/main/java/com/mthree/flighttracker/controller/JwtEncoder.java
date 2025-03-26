package com.mthree.flighttracker.controller;

import org.springframework.stereotype.Component;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

@Component
public class JwtEncoder {

    // Secret key used for token generation and validation
    private static final String SECRET_KEY = "flighttracker-secret-key-2025";

    // Token expiration time in milliseconds (24 hours)
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;


    //Generate a simple token containing username and expiration time

    public String generateToken(String username) {
        // Create token parts
        long expirationTime = System.currentTimeMillis() + EXPIRATION_TIME;

        // Combine username and expiration with a separator
        String tokenData = username + ":" + expirationTime;

        // Create a signature using the secret key
        String signature = createSignature(tokenData);

        // Combine all parts and encode
        String token = tokenData + ":" + signature;
        return Base64.getEncoder().encodeToString(token.getBytes());
    }


    //Validates the token and returns the username if valid, null if invalid

    public String validateToken(String token) {
        try {
            // Decode the token
            String decodedToken = new String(Base64.getDecoder().decode(token));
            String[] parts = decodedToken.split(":");

            // Check if token has all parts
            if (parts.length != 3) {
                return null;
            }

            String username = parts[0];
            long expiration = Long.parseLong(parts[1]);
            String originalSignature = parts[2];

            // Check if token has expired
            if (expiration < System.currentTimeMillis()) {
                return null;
            }

            // Verify signature
            String tokenData = username + ":" + expiration;
            String expectedSignature = createSignature(tokenData);

            if (!expectedSignature.equals(originalSignature)) {
                return null;
            }

            return username;

        } catch (Exception e) {
            return null;
        }
    }


    //Creates a simple signature using the token data and secret key

    private String createSignature(String tokenData) {
        String dataToSign = tokenData + SECRET_KEY;
        return Base64.getEncoder().encodeToString(
                dataToSign.getBytes(StandardCharsets.UTF_8)
        );
    }
}