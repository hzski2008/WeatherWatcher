package com.example.weather.model.converter;

import com.example.weather.model.OpenWeatherMapResponse;
import com.example.weather.model.OpenWeatherMapResponse.Forecast;
import com.example.weather.model.WeatherEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.averagingDouble;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.TreeMap;

@Component("openWeatherMapResponseToEntityConverter")
public class OpenWeatherMapResponseToEntityConverter {
  private static final Logger LOGGER = LoggerFactory.getLogger(OpenWeatherMapResponseToEntityConverter.class);

  // keep only 2 digits after decimal
  private Double convertDouble(Double n) {
    return (int) (Math.round(n * 100)) / 100.0;
  }

  public List<WeatherEntity> buildWeatherEntityFromOpenWeatherMapResponse(List<OpenWeatherMapResponse> responses) {
    List<WeatherEntity> entityRows = new ArrayList<>();
    
    for (OpenWeatherMapResponse resp : responses) {
      List<Forecast> forcasts = resp.getForecasts();
   
      // group forcast objects by date and getting the average temperature from grouped results
      Map<String, Double> groupedResults = forcasts.stream().collect(Collectors.groupingBy(forcast -> {
        String dateTime = forcast.getUtcDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTime, formatter).toLocalDate().toString();
      }, averagingDouble(v->v.getTemperature().getTemp())));

      // sort the grouped results by date (the map key)
      var sortedMap = new TreeMap<String, Double>(groupedResults);
      
      sortedMap.forEach((key, value) -> {
        var entity = new WeatherEntity();
        entity.setDate(LocalDate.parse(key));
        entity.setTemperature(convertDouble(value));
        entity.setLocation(resp.getCity().getName());
        entityRows.add(entity);
      });      
    }
    return entityRows;
  }
  
}