package com.mthree.flighttracker.controller;

import com.mthree.flighttracker.dao.UserDao;
import com.mthree.flighttracker.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtEncoder jwtEncoder;

    // Response class for authentication tokens
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

    // Request class for user registration
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

    // Request class for user login
    public static class UserLoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // Request class for profile updates
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

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody(required = true) UserRegistrationRequest request) {
        try {
            // Check if username already exists
            if (userDao.findByUsername(request.getUsername()) != null) {
                return ResponseEntity.badRequest().body("Username already exists");
            }

            // Create and save new user
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setEmail(request.getEmail());
            userDao.save(user);

            // Generate token and return response
            String token = jwtEncoder.generateToken(user.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AuthResponse(token, user.getUsername()));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody(required = true) UserLoginRequest request) {
        try {
            // Find user and verify password
            User user = userDao.findByUsername(request.getUsername());
            if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }

            // Generate token and return response
            String token = jwtEncoder.generateToken(user.getUsername());
            return ResponseEntity.ok(new AuthResponse(token, user.getUsername()));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(
            @RequestHeader(value = "Authorization", required = true) String token) {
        try {
            // Validate token
            String username = jwtEncoder.validateToken(token);
            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }

            // Get user profile
            User user = userDao.findByUsername(username);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            // Don't send password in response
            user.setPassword(null);
            return ResponseEntity.ok(user);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(
            @RequestHeader(value = "Authorization", required = true) String token,
            @RequestBody(required = true) UserProfileUpdateRequest request) {
        try {
            // Validate token
            String username = jwtEncoder.validateToken(token);
            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }

            // Get user
            User user = userDao.findByUsername(username);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            // Update email if provided
            if (request.getEmail() != null) {
                user.setEmail(request.getEmail());
            }

            // Update password if provided
            if (request.getCurrentPassword() != null && request.getNewPassword() != null) {
                if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid current password");
                }
                user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            }

            // Save updates
            userDao.save(user);

            // Don't send password in response
            user.setPassword(null);
            return ResponseEntity.ok(user);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}