package com.example.weather.consoller;

import com.example.weather.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import static org.springframework.util.StringUtils.capitalize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import com.example.weather.service.IWeatherResponseProduceService;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@Validated
@Tag(name = "weather", description = "The weather API")
public class WeatherController {
 
  @Autowired
  private IWeatherResponseProduceService weatherService;

  // Return true if the given map is empty or all its values are empty. Otherwuse
  // return false
  private Boolean isEmpty(Map<String, List<WeatherResponse>> map) {
    if (CollectionUtils.isEmpty(map)) { // check if map is empty
      return true;
    }
    return map.values().stream().allMatch(v -> CollectionUtils.isEmpty(v)); // return true if all map values are empty
  }

  @Operation(summary = "Return weather info in next 5 days",
    description = "Return weather info in next 5 days for all configured locations e.g, 'api/weather-in-5days'. "
      + "Return weather info in next 5 days for a specific location, e.g, 'api/weather-in-5days?city=Helsinki'. "
      + "The given location must be one of configured locations", 
    tags = { "weather" })
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation"),
    @ApiResponse(responseCode = "404", description = "city not found") })
  @GetMapping("/api/weather-in-5days")
  public Map<String, List<WeatherResponse>> getWeatherForcasts(
    @RequestParam(name = "city", required = false) String city) {

    Map<String, List<WeatherResponse>> storedWeatherResults = weatherService.getWeatherFromDbAndBuildResponseMap(LocalDate.now());

    if (isEmpty(storedWeatherResults)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Weather info not found");
    }
    if (city != null) {
      var value = storedWeatherResults.get(capitalize(city));
      if (value != null) {
        return Map.of(city, value); // return weather by city
      } else {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          "Make sure " + city + " is included in location list in config file");
      }
    }
    return storedWeatherResults; // return weather for all configured locations
  }

}
