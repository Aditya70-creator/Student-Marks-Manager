package com.studentmanagement.repository;

import com.studentmanagement.model.Semester;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// Marks this interface as a Data Access component for handling semesters
@Repository
public interface SemesterRepository extends MongoRepository<Semester, String> {
    
    // Custom Derived Query: Automatically translates into a MongoDB query 
    // to fetch all semesters belonging to a specific logged-in user
    List<Semester> findByUserId(String userId);
}