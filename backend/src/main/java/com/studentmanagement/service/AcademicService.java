package com.studentmanagement.service;

import com.studentmanagement.model.Semester;
import com.studentmanagement.model.Subject;
import com.studentmanagement.repository.SemesterRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AcademicService {

    private final SemesterRepository semesterRepository;

    // Dependency Injection: Spring automatically hooks up our Semester repository here
    public AcademicService(SemesterRepository semesterRepository) {
        this.semesterRepository = semesterRepository;
    }

    /**
     * Logic: Processes subject-level calculations, computes overall SGPA, and saves the semester
     */
    public Semester saveSemester(Semester semester) {
        List<Subject> subjects = semester.getSubjects();
        
        int totalCredits = 0;
        double weightedGradePointsSum = 0;

        // Loop through each dynamic subject row to compute individual metrics
        for (Subject subject : subjects) {
            // 1. Calculate percentage for the subject
            double percentage = (subject.getMarksObtained() / subject.getMaxMarks()) * 100;
            
            // 2. Assign Grade Letter and Points based on standard CBCS scale
            if (percentage >= 90) {
                subject.setGrade("O");
                subject.setGradePoint(10.0);
            } else if (percentage >= 80) {
                subject.setGrade("E");
                subject.setGradePoint(9.0);
            } else if (percentage >= 70) {
                subject.setGrade("A");
                subject.setGradePoint(8.0);
            } else if (percentage >= 60) {
                subject.setGrade("B");
                subject.setGradePoint(7.0);
            } else if (percentage >= 50) {
                subject.setGrade("C");
                subject.setGradePoint(6.0);
            } else if (percentage >= 40) {
                subject.setGrade("D");
                subject.setGradePoint(5.0);
            } else {
                subject.setGrade("F");
                subject.setGradePoint(0.0);
            }

            // 3. Accumulate overall credit numbers
            totalCredits += subject.getCredits();
            weightedGradePointsSum += (subject.getGradePoint() * subject.getCredits());
        }

        // 4. Update parent semester metrics
        semester.setTotalCredits(totalCredits);
        if (totalCredits > 0) {
            double rawSgpa = weightedGradePointsSum / totalCredits;
            // Round precisely to two decimal places
            semester.setSgpa(Math.round(rawSgpa * 100.0) / 100.0);
        } else {
            semester.setSgpa(0.0);
        }

        // 5. Commit everything safely into MongoDB
        return semesterRepository.save(semester);
    }

    /**
     * Logic: Fetches the multi-term academic history list for a specific logged-in user
     */
    public List<Semester> getSemestersByUser(String userId) {
        return semesterRepository.findByUserId(userId);
    }

    /**
     * Logic: Aggregates across all user records to compute live dashboard metrics and insights
     */
    public Map<String, Object> calculateCumulativeAnalytics(String userId) {
        List<Semester> semesters = semesterRepository.findByUserId(userId);
        
        Map<String, Object> analytics = new HashMap<>();
        
        int overallTotalCredits = 0;
        double weightedSgpaSum = 0;
        double highestSgpa = 0;
        double currentSgpa = 0; // SGPA of the most recent semester entered

        if (semesters == null || semesters.isEmpty()) {
            analytics.put("cgpa", 0.0);
            analytics.put("currentSgpa", 0.0);
            analytics.put("highestSgpa", 0.0);
            analytics.put("totalCredits", 0);
            return analytics;
        }

        // Parse through each semester block to determine cumulative trajectory metrics
        for (Semester sem : semesters) {
            overallTotalCredits += sem.getTotalCredits();
            weightedSgpaSum += (sem.getSgpa() * sem.getTotalCredits());
            
            if (sem.getSgpa() > highestSgpa) {
                highestSgpa = sem.getSgpa();
            }
            
            // Keeps updating, so it ultimately retains the value of the last semester item in the collection
            currentSgpa = sem.getSgpa(); 
        }

        // Compute overall CGPA
        double rawCgpa = (overallTotalCredits > 0) ? (weightedSgpaSum / overallTotalCredits) : 0.0;
        double roundedCgpa = Math.round(rawCgpa * 100.0) / 100.0;

        analytics.put("cgpa", roundedCgpa);
        analytics.put("currentSgpa", currentSgpa);
        analytics.put("highestSgpa", highestSgpa);
        analytics.put("totalCredits", overallTotalCredits);

        return analytics;
    }
}