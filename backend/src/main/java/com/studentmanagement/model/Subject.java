package com.studentmanagement.model;

// No @Document annotation here because this class serves as an embedded object layout
public class Subject {

    private String name;          // e.g., "Data Structures and Algorithms"
    private double marksObtained; // Marks scored by the student
    private double maxMarks;      // Maximum achievable marks (e.g., 100)
    private int credits;          // Academic weight of the subject (e.g., 4)
    
    // Calculated Metric Fields (Computed automatically by our future Service layer)
    private String grade;         // Letter grade (e.g., "O", "A", "B")
    private double gradePoint;    // Numerical point on a 10-point scale (e.g., 9.0)

    // 1. Default Empty Constructor (Required by Spring Boot for JSON parsing)
    public Subject() {
    }

    // 2. Parameterized Constructor for quick array building
    public Subject(String name, double marksObtained, double maxMarks, int credits) {
        this.name = name;
        this.marksObtained = marksObtained;
        this.maxMarks = maxMarks;
        this.credits = credits;
    }

    // 3. Getters and Setters (Encapsulation compliance)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMarksObtained() {
        return marksObtained;
    }

    public void setMarksObtained(double marksObtained) {
        this.marksObtained = marksObtained;
    }

    public double getMaxMarks() {
        return maxMarks;
    }

    public void setMaxMarks(double maxMarks) {
        this.maxMarks = maxMarks;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public double getGradePoint() {
        return gradePoint;
    }

    public void setGradePoint(double gradePoint) {
        this.gradePoint = gradePoint;
    }
}