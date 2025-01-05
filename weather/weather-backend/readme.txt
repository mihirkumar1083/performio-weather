# Weather App - Spring Boot Application

This is a Spring Boot application that fetches weather data from the OpenWeatherMap API and returns the weather details such as temperature and weather description in a RESTful response.

## Prerequisites

- **Java 11+** (JDK 11 or later)
- **Maven** (for building and running the project)
- **API_KEY** from OpenWeatherMap (you can get it from [OpenWeatherMap](https://openweathermap.org/api))

## Setup Instructions

### Step 1: Clone the Repository

Clone this repository to your local machine:

```bash
git clone https://github.com/yourusername/weatherapp.git
cd weatherapp

mvn clean install
mvn spring-boot:run -Dspring-boot.run.arguments="--openweathermap.api.key=your_api_key_here"


curl -X GET http://localhost:8080/api/weather/London

mvn test to run unit test