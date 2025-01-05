package com.mycompany.weather.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import com.mycompany.weather.model.WeatherResponse;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {

    @Mock
    private RestTemplate restTemplate;  // Mocking RestTemplate

    @InjectMocks
    private WeatherService weatherService;  // Injecting mocked RestTemplate into WeatherService

    private String validCity = "London";
    private String invalidCity = "InvalidCity";
    private String apiKey = "dummyApiKey";  // Just a placeholder, will be injected

    private String validApiResponse;

    @BeforeEach
    void setUp() {
        // Prepare a valid JSON response that simulates what OpenWeatherMap API would return
        validApiResponse = "{\n" +
                "  \"main\": {\n" +
                "    \"temp\": 15.0\n" +
                "  },\n" +
                "  \"weather\": [\n" +
                "    {\n" +
                "      \"description\": \"clear sky\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        // Mock the behavior of RestTemplate when it makes an API call
        when(restTemplate.getForObject(anyString(), eq(String.class), any(), any())).thenReturn(validApiResponse);
    }

    // Positive Test Case: When the API returns valid data
    @Test
    void testGetWeatherForValidCity() throws Exception {
        // Act
        WeatherResponse response = weatherService.getWeather(validCity);

        // Assert
        assertNotNull(response, "WeatherResponse should not be null");
        assertEquals(15.0, response.getTemp(), "Temperature should match");
        assertEquals("clear sky", response.getDescription(), "Description should match");
        verify(restTemplate, times(1)).getForObject(anyString(), eq(String.class), any(), any());
    }

    // Negative Test Case: When the API returns a null or empty response
    @Test
    void testGetWeatherWithEmptyResponse() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(String.class), any(), any())).thenReturn("");

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            weatherService.getWeather(validCity);
        });

        assertEquals("Empty or null response from OpenWeatherMap API.", exception.getMessage());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(String.class), any(), any());
    }

    // Negative Test Case: When the API throws an error
    @Test
    void testGetWeatherWhenApiThrowsException() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(String.class), any(), any())).thenThrow(new RuntimeException("API error"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            weatherService.getWeather(validCity);
        });

        assertEquals("API error", exception.getMessage());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(String.class), any(), any());
    }

    // Negative Test Case: When the city is invalid or API returns unexpected data
    @Test
    void testGetWeatherForInvalidCity() throws Exception {
        // Arrange: Simulate a different response for invalid city (empty or erroneous data)
        when(restTemplate.getForObject(anyString(), eq(String.class), any(), any())).thenReturn("{ }");

        // Act
        WeatherResponse response = weatherService.getWeather(invalidCity);

        // Assert
        assertNotNull(response, "WeatherResponse should not be null, but check the output");
        assertEquals(0.0, response.getTemp(), "Temperature should default to 0 when API returns empty data");
        assertEquals("", response.getDescription(), "Description should be empty for invalid data");
        verify(restTemplate, times(1)).getForObject(anyString(), eq(String.class), any(), any());
    }
}
