package com.example.weather.model.converter;

import com.example.weather.config.ConfigProperties;
import com.example.weather.model.WeatherEntity;
import com.example.weather.model.WeatherResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("entityToWeatherResponseConverter")
public class EntityMapToWeatherResponseMapConverter {

  @Autowired  
  private ConfigProperties config;

  private String getMeasurementUnitsString() {
    switch (config.getUnits()) {
    case "imperial":
      return " Fahrenheit";
    case "metric":
      return "Celsius";
    default:
      return "Kelvin";
    }
  }
 
  // get corresponding temperatre limit from properties file based on location name
  private Double getLimit(String name) {
      int index = config.getLocations().indexOf(name);
      return config.getThresholds().get(index);
  }

  public Map<String, List<WeatherResponse>> buildWeatherResponseMapFromEntityMap(
    Map<String, List<WeatherEntity>> groupedEntities) {
      
    Map<String, List<WeatherResponse>> map = new HashMap<>();
    groupedEntities.forEach((k, v) -> {
      List<WeatherResponse> list = new ArrayList<>();
      Double limit = getLimit(k);
      
      v.stream().forEach(x -> {
        var weatherResponse = new WeatherResponse();
         
        weatherResponse.setTempature(x.getTemperature());
        weatherResponse.setDate(x.getDate());
        weatherResponse.setThreshold(limit);
        weatherResponse.setUnits(getMeasurementUnitsString());
        weatherResponse.setLimitExceed(x.getTemperature() > limit);
        list.add(weatherResponse);
      });
      map.put(k, list);
    });
    return map;
  }

}
