Weather App - Frontend
Overview
This is the frontend for a Weather Application built with React. It allows users to enter a city, fetch weather data from a backend, and display the temperature and weather description.

Features
User can input a city name and submit the form.
The frontend makes a request to a backend API (assumed to be a Spring Boot application) for weather data.
Displays the temperature and weather description for the entered city.
Handles errors when the city is not found or the backend fails.


Prerequisites
Make sure you have the following installed:

Node.js (v16 or later)
npm (comes with Node.js)
Installation
1. Clone the Repository
Clone the frontend repository to your local machine:
bash
Copy code
git clone https://your-repo-url-here.git
cd weather-frontend
2. Install Dependencies
Install all necessary dependencies for the project:


npm install
This will install the required libraries including React, Axios, and testing dependencies.

Running the Application
1. Start the Development Server
Once the dependencies are installed, you can start the development server:

npm start
This will start the React app on http://localhost:3000. Open this URL in your browser to interact with the Weather App.

2. Test the Application
To run the tests for the application, use the following command:


npm test
This will launch Jest in watch mode and run the test cases defined for the React components, including interaction and API handling tests.

How It Works
1. Input the City Name
The user enters a city name (e.g., "London") in the input field and clicks the "Get Weather" button.
The app makes a GET request to the backend API (assuming it is running at http://localhost:8080/api/weather/{city}).

2. Display Weather Data
On successful API response, the temperature and description of the weather for the entered city are displayed.

3. Error Handling
If the city is not found, an error message ("City not found") is displayed.
If there's an API failure (e.g., backend down, network error), an error message is shown.
Testing Dependencies
The application uses the following testing libraries:

Jest: A testing framework to write and run tests.
React Testing Library: For rendering and interacting with React components.
axios-mock-adapter: To mock Axios API calls during testing.
Test Command
To run tests, use:


npm test
This will run all the test cases in the src/components/WeatherSearch.test.js file and output the results in your terminal.

Sample API Request
To test the weather-fetching feature, ensure the backend is running and accessible at http://localhost:8080. When a city (e.g., London) is entered, the frontend makes the following API request:

GET http://localhost:8080/api/weather/London
The backend should respond with weather data, for example:

json
{
  "temp": 15,
  "description": "clear sky"
}
Notes
Make sure the backend server is up and running at http://localhost:8080 (or update the API URL in the frontend accordingly).
For any issues related to CORS, ensure your backend is configured to allow requests from http://localhost:3000.
Future Improvements
Add more detailed error messages (e.g., "City not found", "Network error").
Implement more features like weather forecasts or additional weather details.
Improve the UI for better responsiveness and design.
License
This project is licensed under the MIT License - see the LICENSE file for details.