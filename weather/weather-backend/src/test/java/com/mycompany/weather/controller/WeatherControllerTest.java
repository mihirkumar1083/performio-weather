package com.mycompany.weather.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.mycompany.weather.model.WeatherResponse;
import com.mycompany.weather.service.WeatherService;

/**
 * Unit tests for the {@link WeatherController} class.
 * This test class verifies that the WeatherController behaves correctly 
 * under different scenarios, including valid and invalid city inputs, 
 * and error handling.
 */
@ExtendWith(MockitoExtension.class)
public class WeatherControllerTest {

    @InjectMocks
    private WeatherController weatherController;  // Controller being tested

    @Mock
    private WeatherService weatherService;  // Mocked service used by the controller

    private MockMvc mockMvc;  // MockMvc to simulate HTTP requests

    /**
     * Initializes the mock objects and sets up MockMvc before each test.
     * This method is executed before each individual test.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(weatherController).build();
    }

    /**
     * Tests the {@link WeatherController#getWeather(String)} method for a valid city input.
     * This test verifies that the controller returns a successful response (200 OK) 
     * with the correct weather data when a valid city is provided.
     *
     * @throws Exception if any unexpected error occurs during the test
     */
    @Test
    public void testGetWeatherSuccess() throws Exception {
        // Create a mock WeatherResponse object
        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setTemp(20.5);
        weatherResponse.setDescription("clear sky");

        // Wrap the WeatherResponse in a ResponseEntity
        ResponseEntity<WeatherResponse> responseEntity = new ResponseEntity<>(weatherResponse, HttpStatus.OK);

        // Mock the service call to return the expected ResponseEntity
        when(weatherService.getWeather("London")).thenReturn(responseEntity);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/weather?city=London")
                .contentType("application/json"))
                .andExpect(status().isOk())  // Verify status code 200 OK
                .andExpect(jsonPath("$.temp").value(20.5))  // Verify temperature in the response
                .andExpect(jsonPath("$.description").value("clear sky"));  // Verify description in the response

        // Verify that the service method was called once with the argument "London"
        verify(weatherService, times(1)).getWeather("London");
    }

    /**
     * Tests the {@link WeatherController#getWeather(String)} method for a missing city input.
     * This test verifies that the controller returns a 404 Not Found status when the city is missing 
     * or an empty string is provided in the request.
     *
     * @throws Exception if any unexpected error occurs during the test
     */
    @Test
    public void testGetWeatherMissingCity() throws Exception {
        // Perform the GET request with an empty city parameter
        mockMvc.perform(get("/weather?city=")
                .contentType("application/json"))
                .andExpect(status().isNotFound());  // Verify status code 404 Not Found
    }

    /**
     * Tests the {@link WeatherController#getWeather(String)} method for an invalid city input.
     * This test verifies that the controller returns a 404 Not Found status when an invalid city is provided.
     *
     * @throws Exception if any unexpected error occurs during the test
     */
    @Test
    public void testGetWeatherInvalidCity() throws Exception {
        // Mock the service call to throw an exception for an invalid city
        when(weatherService.getWeather("InvalidCity")).thenThrow(new Exception("City not found"));

        // Perform the GET request and verify the response
        mockMvc.perform(get("/weather?city=InvalidCity")
                .contentType("application/json"))
                .andExpect(status().isNotFound())  // Verify status code 404 Not Found
                .andExpect(jsonPath("$.error").value("City not found"));  // Verify error message in response

        // Verify that the service method was called once with the argument "InvalidCity"
        verify(weatherService, times(1)).getWeather("InvalidCity");
    }
}
