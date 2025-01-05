package com.mycompany.weather.service;

import com.mycompany.weather.model.WeatherResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WeatherService fetches weather data from OpenWeatherMap API.
 */
@Service
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    @Value("${openweathermap.api.key}")
    private String apiKey;

    private static final String URL = "http://api.openweathermap.org/data/2.5/weather?q={city}&appid={apiKey}&units=metric";
    
    private final RestTemplate restTemplate;

    // Constructor injection for RestTemplate
    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Retrieves weather data for a city and parses it into a WeatherResponse object.
     *
     * @param city the name of the city.
     * @return the WeatherResponse containing temperature and weather description.
     * @throws Exception if an error occurs during API request or response parsing.
     */
    public ResponseEntity<WeatherResponse> getWeather(String city) throws Exception {
        // Check if city is missing
        if (city == null || city.trim().isEmpty()) {
            logger.error("City name is missing.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            String response = restTemplate.getForObject(URL, String.class, city, apiKey);

            if (response == null || response.isEmpty()) {
                throw new Exception("Empty or null response from OpenWeatherMap API.");
            }

            Gson gson = new Gson();
            JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);

            // Check if API returned an error (e.g., 404 for invalid city)
            if (jsonResponse.has("cod") && jsonResponse.get("cod").getAsInt() != 200) {
                String errorMessage = jsonResponse.get("message").getAsString();
                logger.error("Error fetching weather data: {}", errorMessage);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Return 404 if city not found
            }

            double temp = jsonResponse.getAsJsonObject("main").get("temp").getAsDouble();
            String description = jsonResponse.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();

            WeatherResponse weatherResponse = new WeatherResponse();
            weatherResponse.setTemp(temp);
            weatherResponse.setDescription(description);

            logger.info("Fetched weather data for city: {} with temp: {}°C", city, temp);

            return new ResponseEntity<>(weatherResponse, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Failed to fetch weather data for city: {}", city, e);
            throw new Exception("Error occurred while retrieving weather data.", e);
        }
    }
}
