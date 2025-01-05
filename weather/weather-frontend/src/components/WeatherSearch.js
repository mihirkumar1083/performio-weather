import React, { useState } from 'react';
import axios from 'axios';

const WeatherSearch = () => {
    const [city, setCity] = useState('');
    const [weatherData, setWeatherData] = useState(null);
    const [error, setError] = useState('');

    const handleCityChange = (event) => {
        setCity(event.target.value);
    };

    const fetchWeather = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/api/weather/${city}`);
            setWeatherData(response.data);
            setError('');
        } catch (err) {
            setError('City not found');
            setWeatherData(null);
        }
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        fetchWeather();
    };

    return (
        <div className="weather-search">
            <h1>Weather Search</h1>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    placeholder="Enter city"
                    value={city}
                    onChange={handleCityChange}
                    required
                />
                <button type="submit">Get Weather</button>
            </form>

            {error && <p className="error">{error}</p>}

            {weatherData && (
                <div className="weather-result">
                    <h2>Weather in {city}</h2>
                    <p>Temperature: {weatherData.temp}°C</p>
                    <p>Description: {weatherData.description}</p>
                </div>
            )}
        </div>
    );
};

export default WeatherSearch;
