package com.mycompany.weather.controller;

import com.mycompany.weather.model.WeatherResponse;
import com.mycompany.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * WeatherController handles HTTP requests related to weather data.
 * It provides an endpoint to retrieve weather information for a specified city.
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    /**
     * Endpoint to get the current weather for a specified city.
     * This method handles cases where the city is missing or invalid.
     *
     * @param city the name of the city to fetch the weather for.
     * @return a ResponseEntity containing the WeatherResponse with the temperature and weather description,
     *         or an error response with an appropriate HTTP status.
     */
    @GetMapping("/weather")
    public ResponseEntity<WeatherResponse> getWeather(@RequestParam(required = false) String city) {
        if (city == null || city.trim().isEmpty()) {
            // If the city is missing or empty, return 404 Not Found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            // Call the WeatherService to get the weather information
        	ResponseEntity<WeatherResponse> weatherResponse = weatherService.getWeather(city);
            return weatherResponse;
        } catch (Exception e) {
            // If an error occurs (e.g., city not found or API failure), return 404 with a generic message
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
