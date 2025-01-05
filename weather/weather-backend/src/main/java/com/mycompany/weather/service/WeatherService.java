package com.mycompany.weather.service;

import com.mycompany.weather.model.WeatherResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * WeatherService fetches weather data from OpenWeatherMap API.
 */
@Service
public class WeatherService {

    @Value("${openweathermap.api.key}")
    private String apiKey;

    private static final String URL = "http://api.openweathermap.org/data/2.5/weather?q={city}&appid={apiKey}&units=metric";

    /**
     * Retrieves weather data for a city and parses it into a WeatherResponse object.
     *
     * @param city the name of the city.
     * @return the WeatherResponse containing temperature and weather description.
     * @throws Exception if an error occurs during API request or response parsing.
     */
    public WeatherResponse getWeather(String city) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(URL, String.class, city, apiKey);

        if (response == null || response.isEmpty()) {
            throw new Exception("Empty or null response from OpenWeatherMap API.");
        }

        Gson gson = new Gson();
        JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);

        double temp = jsonResponse.getAsJsonObject("main").get("temp").getAsDouble();
        String description = jsonResponse.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();

        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setTemp(temp);
        weatherResponse.setDescription(description);

        return weatherResponse;
    }
}
