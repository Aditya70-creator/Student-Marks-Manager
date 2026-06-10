package com.studentmanagement.controller;

import com.studentmanagement.model.Student;
import com.studentmanagement.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 1. Tags this class as a REST API Controller
@RestController
// 2. Defines the base URL for this entire controller
@RequestMapping("/api/students")
// 3. Crucial for connecting separate Frontend and Backend servers
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentService service;

    // Constructor Injection: Handing the Service (Chef) to the Controller (Waiter)
    public StudentController(StudentService service) {
        this.service = service;
    }

    // CREATE: Handle POST requests to add a new student
    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return service.saveStudent(student);
    }

    // READ ALL: Handle GET requests to fetch all students
    @GetMapping
    public List<Student> getAllStudents() {
        return service.getAllStudents();
    }

    // READ ONE: Handle GET requests to fetch a specific student by roll number
    @GetMapping("/{rollNumber}")
    public Student getStudentByRollNumber(@PathVariable String rollNumber) {
        return service.getStudentByRollNumber(rollNumber);
    }

    // DELETE: Handle DELETE requests to remove a student
    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable String id) {
        service.deleteStudent(id);
    }
}