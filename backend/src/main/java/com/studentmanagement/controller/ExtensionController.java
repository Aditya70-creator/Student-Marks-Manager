package com.studentmanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/extensions")
@CrossOrigin(origins = "*")
public class ExtensionController {

    /**
     * Blueprint Stub Gateway for all incoming module calls
     */
    @GetMapping("/{moduleKey}/{userId}")
    public ResponseEntity<Map<String, String>> evaluateExtensionPipeline(
            @PathVariable String moduleKey, 
            @PathVariable String userId) {
        
        Map<String, String> response = new HashMap<>();
        response.put("module", moduleKey.toUpperCase());
        
        // Return a clean system architecture confirmation message
        switch (moduleKey.toLowerCase()) {
            case "pdf" -> response.put("message", "PDF Engine Placeholder: Ready for iText or JasperReports compilation pipelines.");
            case "ai" -> response.put("message", "AI Predictor Placeholder: Ready for Python linear regression or ONNX model hooks.");
            case "resume" -> response.put("message", "CV Builder Placeholder: Ready for modular markdown data templating algorithms.");
            case "placement" -> response.put("message", "Placement Tracker Placeholder: Ready for corporate vacancy mapping arrays.");
            default -> response.put("message", "Unknown system extension code.");
        }

        // Return HTTP 501 (Not Implemented) to cleanly signify a staging structural state
        return new ResponseEntity<>(response, HttpStatus.NOT_IMPLEMENTED);
    }
}