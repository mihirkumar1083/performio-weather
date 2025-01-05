package com.mycompany.weather.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HealthController is used to check the status of the backend application.
 */
@RestController
public class HealthController {

    /**
     * Endpoint to check the health of the backend application.
     * 
     * @return A simple string message indicating the health status of the application.
     */
    @GetMapping("/health")
    public String healthCheck() {
        return "Backend is running";
    }
}
