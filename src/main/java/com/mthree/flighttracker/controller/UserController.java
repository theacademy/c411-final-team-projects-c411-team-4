package com.mthree.flighttracker.controller;

import com.mthree.flighttracker.dao.UserDao;
import com.mthree.flighttracker.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Base64;

// JWT token handling with Base64, confirm if this is right
@RestController
@RequestMapping("/api/users")
public class UserController {

    // Added constants for JWT token generation and validation
    private static final String SECRET_KEY = "mySecretKey123";
    private static final long EXPIRATION_TIME = 864_000_000; // 10 days

    // Added required dependencies for user management and password encryption
    @Autowired
    private UserDao userDao;

    private PasswordEncoder passwordEncoder;

    // Added response class for authentication tokens
    private static class AuthResponse {
        private String token;
        private String username;

        public AuthResponse(String token, String username) {
            this.token = token;
            this.username = username;
        }

        public String getToken() { return token; }
        public String getUsername() { return username; }
    }

    // Kept original request class, no changes needed
    public static class UserRegistrationRequest {
        private String username;
        private String password;
        private String email;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    // Kept original request class, no changes needed
    public static class UserLoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // Kept original request class, no changes needed
    public static class UserProfileUpdateRequest {
        private String email;
        private String currentPassword;
        private String newPassword;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getCurrentPassword() { return currentPassword; }
        public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }

    // Added method to generate JWT tokens using Base64 encoding
    private String generateToken(String username) {
        long expirationTime = System.currentTimeMillis() + EXPIRATION_TIME;
        String signature = Base64.getEncoder().encodeToString(
                (username + ":" + expirationTime + ":" + SECRET_KEY).getBytes()
        );
        String token = username + ":" + expirationTime + ":" + signature;
        return Base64.getEncoder().encodeToString(token.getBytes());
    }

    // Added method to validate JWT tokens and extract username
    private String validateToken(String token) {
        try {
            String decoded = new String(Base64.getDecoder().decode(token));
            String[] parts = decoded.split(":");

            if (parts.length != 3) return null;

            String username = parts[0];
            long expiration = Long.parseLong(parts[1]);
            String signature = parts[2];

            if (expiration < System.currentTimeMillis()) return null;

            String expectedSignature = Base64.getEncoder().encodeToString(
                    (username + ":" + expiration + ":" + SECRET_KEY).getBytes()
            );

            return signature.equals(expectedSignature) ? username : null;
        } catch (Exception e) {
            return null;
        }
    }

    // Implemented registration logic with password encryption and token generation
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody(required = true) UserRegistrationRequest request) {
        try {
            if (userDao.findByUsername(request.getUsername()) != null) {
                return ResponseEntity.badRequest().body("Username already exists");
            }

            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setEmail(request.getEmail());
            userDao.save(user);

            String token = generateToken(user.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AuthResponse(token, user.getUsername()));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Implemented login logic with password verification and token generation
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody(required = true) UserLoginRequest request) {
        try {
            User user = userDao.findByUsername(request.getUsername());
            if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }

            String token = generateToken(user.getUsername());
            return ResponseEntity.ok(new AuthResponse(token, user.getUsername()));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Implemented profile retrieval with token validation
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(
            @RequestHeader(value = "Authorization", required = true) String token) {
        try {
            String username = validateToken(token);
            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }

            User user = userDao.findByUsername(username);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            user.setPassword(null); // Don't send password
            return ResponseEntity.ok(user);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Implemented profile update with token validation and password change functionality
    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(
            @RequestHeader(value = "Authorization", required = true) String token,
            @RequestBody(required = true) UserProfileUpdateRequest request) {
        try {
            String username = validateToken(token);
            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }

            User user = userDao.findByUsername(username);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            if (request.getEmail() != null) {
                user.setEmail(request.getEmail());
            }

            if (request.getCurrentPassword() != null && request.getNewPassword() != null) {
                if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid current password");
                }
                user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            }

            userDao.save(user);
            user.setPassword(null); // Don't send password
            return ResponseEntity.ok(user);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}