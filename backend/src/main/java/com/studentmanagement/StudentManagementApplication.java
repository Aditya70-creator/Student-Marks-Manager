package com.studentmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// This is the magic annotation that configures everything
@SpringBootApplication
public class StudentManagementApplication {

    // The main method: The exact starting line of the program
    public static void main(String[] args) {
        SpringApplication.run(StudentManagementApplication.class, args);
        System.out.println("Backend is running successfully on port 8080!");
    }

}