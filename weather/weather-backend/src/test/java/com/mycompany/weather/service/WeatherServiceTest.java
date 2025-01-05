package com.mycompany.weather.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.mycompany.weather.model.WeatherResponse;

/**
 * Unit tests for the {@link WeatherService} class.
 *
 * This test class verifies the functionality of the {@link WeatherService} methods,
 * ensuring correct behavior in various scenarios such as valid city inputs,
 * missing or invalid city inputs, API errors, and empty responses.
 * The tests use {@link Mockito} to mock dependencies like the {@link RestTemplate} 
 * to avoid making actual API calls and simulate different conditions.
 */
class WeatherServiceTest {

    @Mock
    private RestTemplate restTemplate; // Mocked RestTemplate for simulating API calls

    @InjectMocks
    private WeatherService weatherService; // The service class being tested

    /**
     * Initializes the mock objects before each test.
     * This method is executed before each individual test to set up the mocks.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the {@link WeatherService#getWeather(String)} method when a valid city is provided.
     * This test verifies that the service fetches weather data correctly for a valid city
     * and returns the expected weather response with status code 200 OK.
     *
     * @throws Exception if any unexpected error occurs during the test
     */
    @Test
    void testGetWeather_ValidCity() throws Exception {
        // Prepare the mock response from OpenWeatherMap API
        String city = "London";
        String apiResponse = "{ \"main\": { \"temp\": 15.0 }, \"weather\": [{ \"description\": \"clear sky\" }], \"cod\": 200 }";
        
        // Mock the behavior of restTemplate
        when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(String.class), Mockito.any(), Mockito.any()))
                .thenReturn(apiResponse);

        // Call the service method
        ResponseEntity<WeatherResponse> responseEntity = weatherService.getWeather(city);

        // Verify the status and the response body
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(15.0, responseEntity.getBody().getTemp());
        assertEquals("clear sky", responseEntity.getBody().getDescription());
    }

    /**
     * Tests the {@link WeatherService#getWeather(String)} method when the city is missing.
     * This test verifies that if the city is not provided (empty or null), the service 
     * returns a 404 Not Found HTTP status with no body.
     *
     * @throws Exception if any unexpected error occurs during the test
     */
    @Test
    void testGetWeather_MissingCity() throws Exception {
        // Call the service method with an empty city
        ResponseEntity<WeatherResponse> responseEntity = weatherService.getWeather("");

        // Verify that the response is a 404 Not Found
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    /**
     * Tests the {@link WeatherService#getWeather(String)} method when an invalid city is provided.
     * This test verifies that if the city is invalid (e.g., non-existent), the service 
     * handles the error correctly and returns a 404 Not Found HTTP status.
     *
     * @throws Exception if any unexpected error occurs during the test
     */
    @Test
    void testGetWeather_InvalidCity() throws Exception {
        // Prepare a mock response for an invalid city (404 from OpenWeatherMap)
        String city = "InvalidCity";
        String apiResponse = "{ \"cod\": 404, \"message\": \"city not found\" }";

        when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(String.class), Mockito.any(), Mockito.any()))
                .thenReturn(apiResponse);

        // Call the service method
        ResponseEntity<WeatherResponse> responseEntity = weatherService.getWeather(city);

        // Verify the response is a 404 Not Found
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    /**
     * Tests the {@link WeatherService#getWeather(String)} method when the API call fails due to an error.
     * This test simulates a scenario where the API call to OpenWeatherMap fails (e.g., network issue),
     * and verifies that the service handles the exception correctly.
     *
     * @throws Exception if any unexpected error occurs during the test
     */
    @Test
    void testGetWeather_ApiError() throws Exception {
        // Simulate an API failure (e.g., network issue)
        String city = "London";
        when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(String.class), Mockito.any(), Mockito.any()))
                .thenThrow(new RuntimeException("API call failed"));

        // Call the service method
        try {
            weatherService.getWeather(city);
            fail("Expected exception was not thrown");
        } catch (Exception e) {
            assertEquals("Error occurred while retrieving weather data.", e.getMessage());
        }
    }

    /**
     * Tests the {@link WeatherService#getWeather(String)} method when the API response is empty.
     * This test simulates a scenario where OpenWeatherMap returns an empty response,
     * and verifies that the service throws an appropriate exception for this case.
     *
     * @throws Exception if any unexpected error occurs during the test
     */
    @Test
    void testGetWeather_EmptyApiResponse() throws Exception {
        // Simulate an empty API response
        String city = "London";
        when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(String.class), Mockito.any(), Mockito.any()))
                .thenReturn("");

        // Call the service method
        try {
            weatherService.getWeather(city);
            fail("Expected exception was not thrown");
        } catch (Exception e) {
            assertEquals("Empty or null response from OpenWeatherMap API.", e.getMessage());
        }
    }
}
