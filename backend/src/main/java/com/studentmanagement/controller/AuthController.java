package com.studentmanagement.controller;

import com.studentmanagement.model.User;
import com.studentmanagement.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Allows your frontend server to communicate with this controller safely
public class AuthController {

    private final AuthService authService;

    // Constructor Injection: Attaching our secure authentication brain to this web controller
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint: SIGNUP (/api/auth/signup)
     * Receives a JSON profile payload and registers a new student user
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        try {
            User registeredUser = authService.registerUser(user);
            // Hide the hashed password from the network response for security compliance
            registeredUser.setPassword(null); 
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (RuntimeException error) {
            // If the email is a duplicate, return a clean error message back to the UI
            Map<String, String> response = new HashMap<>();
            response.put("message", error.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint: LOGIN (/api/auth/login)
     * Validates incoming credentials and returns user identity states
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginPayload) {
        User validatedUser = authService.loginUser(loginPayload.getEmail(), loginPayload.getPassword());

        if (validatedUser != null) {
            // Authentication successful! Wipe password string before wire return
            validatedUser.setPassword(null);
            return new ResponseEntity<>(validatedUser, HttpStatus.OK);
        } else {
            // Authentication failed! Return an unauthorized message
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid email credentials or password matching mismatch.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Inner Static Data Object: Structures incoming JSON parameters for clean login validation parsing
     */
    public static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}