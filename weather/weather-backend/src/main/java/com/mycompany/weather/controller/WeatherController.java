package com.mycompany.weather.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.weather.model.WeatherResponse;
import com.mycompany.weather.service.WeatherService;

/**
 * WeatherController handles HTTP requests related to weather data.
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    /**
     * Endpoint to get the current weather for a specified city.
     * 
     * @param city the name of the city to fetch the weather for.
     * @return a WeatherResponse containing the temperature and weather description.
     * @throws Exception if there is an issue retrieving the weather.
     */
    @GetMapping("/weather")
    public WeatherResponse getWeather(@RequestParam String city) throws Exception {
        return weatherService.getWeather(city);
    }
}