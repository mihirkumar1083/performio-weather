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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.mycompany.weather.model.WeatherResponse;
import com.mycompany.weather.service.WeatherService;

@ExtendWith(MockitoExtension.class)
public class WeatherControllerTest {

    @InjectMocks
    private WeatherController weatherController;

    @Mock
    private WeatherService weatherService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(weatherController).build();
    }

    @Test
    public void testGetWeatherSuccess() throws Exception {
        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setTemp(20.5);
        weatherResponse.setDescription("clear sky");

        when(weatherService.getWeather("London")).thenReturn(weatherResponse);

        mockMvc.perform(get("/weather?city=London")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.temp").value(20.5))
                .andExpect(jsonPath("$.description").value("clear sky"));

        verify(weatherService, times(1)).getWeather("London");
    }

    @Test
    public void testGetWeatherFailure() throws Exception {
        when(weatherService.getWeather("InvalidCity")).thenThrow(new Exception("City not found"));

        mockMvc.perform(get("/weather?city=InvalidCity")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("City not found"));

        verify(weatherService, times(1)).getWeather("InvalidCity");
    }
}
