package com.studentmanagement.service;

import com.studentmanagement.model.Student;
import com.studentmanagement.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Registers this class as the "Business Logic" provider in Spring Boot
@Service
public class StudentService {

    // The Service needs the Repository to save the final data
    private final StudentRepository repository;

    // Constructor Injection: Spring Boot automatically provides the Repository here
    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    // 1. ADD / UPDATE A STUDENT
    public Student saveStudent(Student student) {
        
        // --- Core Business Logic: Calculations ---
        double total = student.getPhysicsMarks() + student.getChemistryMarks() + student.getMathMarks();
        student.setTotalMarks(total);

        // Calculate percentage and round to 2 decimal places
        double percentage = total / 3.0;
        percentage = Math.round(percentage * 100.0) / 100.0;
        student.setPercentage(percentage);

        // Determine Grade
        if (percentage >= 90) {
            student.setGrade("O"); // Outstanding
        } else if (percentage >= 80) {
            student.setGrade("E"); // Excellent
        } else if (percentage >= 70) {
            student.setGrade("A");
        } else if (percentage >= 60) {
            student.setGrade("B");
        } else if (percentage >= 50) {
            student.setGrade("C");
        } else {
            student.setGrade("F"); // Fail
        }

        // The math is done. Hand it to the repository to save in MongoDB.
        return repository.save(student);
    }

    // 2. GET ALL STUDENTS
    public List<Student> getAllStudents() {
        return repository.findAll(); // A pre-built MongoDB command
    }

    // 3. GET A SPECIFIC STUDENT BY ROLL NUMBER
    public Student getStudentByRollNumber(String rollNumber) {
        return repository.findByRollNumber(rollNumber); // Our custom MongoDB command
    }

    // 4. DELETE A STUDENT
    public void deleteStudent(String id) {
        repository.deleteById(id);
    }
}