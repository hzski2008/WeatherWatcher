# WeatherWatcher

This is a Sping Boot application which monitors weather forecasts for specific locations. The application 1) periodically fetches weather forecasts in the next 5 days for configured locations from the external weather service provider, 2) checks the fetched temperature against configured temperature thresholds and store the results and 3) returns the stored results via REST API. 

To build project from the source directory: `./mvnw clean install` or `./mvnw package`

To run the application: `./mvnw spring-boot:run`

To run API tests: start the application and then `./mvnw -Dtest=apitest.WeatherApiTest test`

To run unit tests: `./mvnw test`

H2 console: http://localhost:8080/h2-console/

API documentation/Swagger): Sart the application and go to http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config  
