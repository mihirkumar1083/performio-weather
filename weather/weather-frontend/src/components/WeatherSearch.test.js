import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import WeatherSearch from './WeatherSearch';
import axios from 'axios';
import MockAdapter from 'axios-mock-adapter';

// Mocking Axios for API requests
const mock = new MockAdapter(axios);

describe('WeatherSearch Component', () => {
  
  // Reset axios mock before each test
  beforeEach(() => {
    mock.reset();
  });

  // Test 1: Component renders correctly
  it('should render the WeatherSearch component', () => {
    render(<WeatherSearch />);
    
    expect(screen.getByText('Weather Search')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Enter city')).toBeInTheDocument();
    expect(screen.getByText('Get Weather')).toBeInTheDocument();
  });

  // Test 2: Successful weather data fetch
  it('should fetch and display weather data when a valid city is entered', async () => {
    const mockWeatherData = { temp: 25, description: 'clear sky' };
    
    // Mock the API response
    mock.onGet('http://localhost:8080/api/weather/London').reply(200, mockWeatherData);

    render(<WeatherSearch />);
    
    const input = screen.getByPlaceholderText('Enter city');
    const button = screen.getByText('Get Weather');
    
    // Simulate user input and form submission
    userEvent.type(input, 'London');
    userEvent.click(button);

    // Wait for the API request to finish
    await waitFor(() => {
      expect(screen.getByText('Weather in London')).toBeInTheDocument();
      expect(screen.getByText('Temperature: 25°C')).toBeInTheDocument();
      expect(screen.getByText('Description: clear sky')).toBeInTheDocument();
    });
  });

  // Test 3: Error handling when city is not found
  it('should show an error message when the city is not found', async () => {
    // Mock an API error response
    mock.onGet('http://localhost:8080/api/weather/InvalidCity').reply(404);

    render(<WeatherSearch />);
    
    const input = screen.getByPlaceholderText('Enter city');
    const button = screen.getByText('Get Weather');
    
    // Simulate user input and form submission
    userEvent.type(input, 'InvalidCity');
    userEvent.click(button);

    // Wait for the error message to appear
    await waitFor(() => {
      expect(screen.getByText('City not found')).toBeInTheDocument();
    });
  });

  // Test 4: Error handling when API request fails (network error)
  it('should show an error message when the API request fails', async () => {
    // Simulate network error
    mock.onGet('http://localhost:8080/api/weather/London').networkError();

    render(<WeatherSearch />);
    
    const input = screen.getByPlaceholderText('Enter city');
    const button = screen.getByText('Get Weather');
    
    // Simulate user input and form submission
    userEvent.type(input, 'London');
    userEvent.click(button);

    // Wait for the error message to appear
    await waitFor(() => {
      expect(screen.getByText('City not found')).toBeInTheDocument();
    });
  });

  // Test 5: Check if the input field is cleared after form submission
  it('should clear the input field after form submission', async () => {
    const mockWeatherData = { temp: 25, description: 'clear sky' };

    // Mock the API response
    mock.onGet('http://localhost:8080/api/weather/London').reply(200, mockWeatherData);

    render(<WeatherSearch />);
    
    const input = screen.getByPlaceholderText('Enter city');
    const button = screen.getByText('Get Weather');

    // Simulate user input and form submission
    userEvent.type(input, 'London');
    userEvent.click(button);

    // Wait for the API request to finish
    await waitFor(() => {
      expect(screen.getByText('Weather in London')).toBeInTheDocument();
    });

    // Check if input field is cleared after submitting
    expect(input.value).toBe('');
  });
});
