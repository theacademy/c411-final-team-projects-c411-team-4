package com.mthree.flighttracker.controller;

import com.mthree.flighttracker.dao.UserDao;
import com.mthree.flighttracker.model.User;
import com.mthree.flighttracker.service.UserHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserHistoryService userHistoryService;
    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtEncoder jwtEncoder;

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
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/history")
    public ResponseEntity<?> getSearchHistory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(userHistoryService.getHistoryByUsername(authentication.getName()));
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
            ResponseCookie jwtCookie = ResponseCookie
                    .from("flight_token", token)
                    .maxAge(Duration.ofDays(7))
                    .httpOnly(true)
                    .path("/")
                    .build();
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie removeTokenCookie = ResponseCookie
                .from("flight_token", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, removeTokenCookie.toString())
                .build();
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
            ResponseCookie jwtCookie = ResponseCookie
                    .from("flight_token", token)
                    .httpOnly(true)
                    .maxAge(Duration.ofDays(7))
                    .path("/")
                    .build();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userDao.findByUsername(auth.getName());
        if (user == null) {
            // this should be a server error if we auth a jwt
            // but the user for the jwt doesnt exist
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        // Don't send password in response
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@RequestBody(required = true) UserProfileUpdateRequest request) {
        try {
            final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userDao.findByUsername(auth.getName());
            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            if (request.getEmail() != null) {
                user.setEmail(request.getEmail());
            }

            if (request.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            }

            userDao.save(user);

            user.setPassword(null);
            return ResponseEntity.ok(user);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}