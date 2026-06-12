package com.studentmanagement.repository;

import com.studentmanagement.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

// Registers this interface as a data repository bean in the Spring application context
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    // 1. Find a user profile by their email address
    User findByEmail(String email);
    
    // 2. Find a user profile by their unique username
    User findByUsername(String username);
    
    // 3. Quick boolean check to see if an email already exists in the database
    boolean existsByEmail(String email);
}