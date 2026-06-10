package com.studentmanagement.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Tells MongoDB that objects of this class should be saved in a collection called "students"
@Document(collection = "students")
public class Student {

    // The unique identifier for the database
    @Id
    private String id;

    // Student Details
    private String name;
    private String rollNumber;

    // Marks
    private double physicsMarks;
    private double chemistryMarks;
    private double mathMarks;

    // Calculated Fields
    private double totalMarks;
    private double percentage;
    private String grade;

    // 1. Default Constructor (Required by Spring Boot)
    public Student() {
    }

    // 2. Getters and Setters
    // Because our variables are "private" (hidden for security), 
    // we use these public methods to let other files read or change the data safely.

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

    public double getPhysicsMarks() { return physicsMarks; }
    public void setPhysicsMarks(double physicsMarks) { this.physicsMarks = physicsMarks; }

    public double getChemistryMarks() { return chemistryMarks; }
    public void setChemistryMarks(double chemistryMarks) { this.chemistryMarks = chemistryMarks; }

    public double getMathMarks() { return mathMarks; }
    public void setMathMarks(double mathMarks) { this.mathMarks = mathMarks; }

    public double getTotalMarks() { return totalMarks; }
    public void setTotalMarks(double totalMarks) { this.totalMarks = totalMarks; }

    public double getPercentage() { return percentage; }
    public void setPercentage(double percentage) { this.percentage = percentage; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
}