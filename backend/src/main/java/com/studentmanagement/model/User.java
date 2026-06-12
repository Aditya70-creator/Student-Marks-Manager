package com.studentmanagement.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Tells MongoDB to store objects of this class inside a new collection called "users"
@Document(collection = "users")
public class User {

    @Id
    private String id; // Unique identity token generated automatically by MongoDB
    
    private String username;
    private String email;
    private String password; // This will store the securely encrypted hash string, never raw text

    // 1. Default Empty Constructor (Required by Spring Boot behind the scenes)
    public User() {
    }

    // 2. Parameterized Constructor for easy object instantiation
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // 3. Getters and Setters (Provides clean encapsulation)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}