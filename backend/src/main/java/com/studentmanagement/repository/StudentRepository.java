package com.studentmanagement.repository;

import com.studentmanagement.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

// Marks this file as a Data Access component in the Spring architecture
@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
    
    // Spring Boot is smart enough to generate the MongoDB query just by reading this method name!
    Student findByRollNumber(String rollNumber);

}