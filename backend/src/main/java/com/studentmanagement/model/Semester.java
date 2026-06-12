package com.studentmanagement.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

// Tells MongoDB to store objects of this class inside a new collection called "semesters"
@Document(collection = "semesters")
public class Semester {

    @Id
    private String id;              // Unique identity token for this semester document
    private String userId;          // Connects this semester to a unique student account id
    private String semesterName;    // e.g., "Semester 1", "Semester 2"
    
    // The embedded sub-document list containing all subjects added inside this semester
    private List<Subject> subjects = new ArrayList<>();
    
    // Calculated Summary Metrics for this semester block
    private int totalCredits;
    private double sgpa;

    // 1. Default Empty Constructor
    public Semester() {
    }

    // 2. Parameterized Constructor
    public Semester(String userId, String semesterName) {
        this.userId = userId;
        this.semesterName = semesterName;
    }

    // 3. Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public int getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }

    public double getSgpa() {
        return sgpa;
    }

    public void setSgpa(double sgpa) {
        this.sgpa = sgpa;
    }
}