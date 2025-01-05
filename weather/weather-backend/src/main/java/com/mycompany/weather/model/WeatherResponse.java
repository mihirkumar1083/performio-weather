package com.mycompany.weather.model;

/**
 * The WeatherResponse class holds weather-related data for a specific city.
 * It contains the temperature in Celsius and the weather description.
 */
public class WeatherResponse {

    private double temp;
    private String description;

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
