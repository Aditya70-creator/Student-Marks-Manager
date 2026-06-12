package com.studentmanagement.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studentmanagement.model.Semester;
import com.studentmanagement.service.AcademicService;

@RestController
@RequestMapping("/api/academic")
@CrossOrigin(origins = "*") // Prevents browser CORS blockage when interacting with your local frontend
public class AcademicController {

    private final AcademicService academicService;

    // Dependency Injection: Connecting the academic calculation service layer
    public AcademicController(AcademicService academicService) {
        this.academicService = academicService;
    }

    /**
     * Endpoint: SAVE SEMESTER (/api/academic/save)
     * Compiles, runs calculations, and commits a semester block with all its dynamic subjects
     */
    @PostMapping("/save")
    public ResponseEntity<Semester> saveSemester(@RequestBody Semester semester) {
        Semester savedRecord = academicService.saveSemester(semester);
        return new ResponseEntity<>(savedRecord, HttpStatus.CREATED);
    }

    /**
     * Endpoint: FETCH USER SEMESTERS (/api/academic/user/{userId})
     * Pulls up the complete multi-term historic timeline for a single logged-in student account
     */
    @GetMapping("/user/{userId}") // Fixed capital 'G'
    public ResponseEntity<List<Semester>> getSemestersByUser(@PathVariable String userId) {
        List<Semester> historyList = academicService.getSemestersByUser(userId);
        return new ResponseEntity<>(historyList, HttpStatus.OK);
    }

    /**
     * Endpoint: CUMULATIVE METRICS INSIGHTS (/api/academic/analytics/{userId})
     * Computes overall running CGPA, cumulative credits, and peak tracking parameters
     */
    @GetMapping("/analytics/{userId}") // Fixed capital 'G'
    public ResponseEntity<Map<String, Object>> getCumulativeAnalytics(@PathVariable String userId) {
        Map<String, Object> metricsMap = academicService.calculateCumulativeAnalytics(userId);
        return new ResponseEntity<>(metricsMap, HttpStatus.OK);
    }
}