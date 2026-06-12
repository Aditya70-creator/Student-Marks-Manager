package com.studentmanagement.service;

import com.studentmanagement.model.User;
import com.studentmanagement.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    // Dependency Injection: Spring Boot wires our UserRepository worker automatically here
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Business Logic: Registers a brand new student user account profile safely
     */
    public User registerUser(User user) {
        // Rule 1: Check for duplicate accounts
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("An account profile is already mapped to this email address.");
        }

        // Rule 2: Hash the plain text password before it touches the network wire or DB
        // BCrypt.gensalt() generates a unique random string (salt) to make the hash unbreakable
        String secureHashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(secureHashedPassword);

        // Save the secured User instance into MongoDB
        return userRepository.save(user);
    }

    /**
     * Business Logic: Verifies login credentials submitted by a user
     * Returns the full User profile if successful, or null if validation fails
     */
    public User loginUser(String email, String rawPassword) {
        // Step 1: Look up the user by their unique identity email
        User user = userRepository.findByEmail(email);

        // Step 2: If user doesn't exist, exit immediately
        if (user == null) {
            return null; 
        }

        // Step 3: Use BCrypt to compare the raw incoming password text with the saved secure hash string
        if (BCrypt.checkpw(rawPassword, user.getPassword())) {
            return user; // Passwords match! Return the validated user profile
        }

        return null; // Passwords mismatch
    }
}