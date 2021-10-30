package com.example.weather.service;

import com.example.weather.model.converter.EntityMapToWeatherResponseMapConverter;
import com.example.weather.model.WeatherEntity;
import com.example.weather.model.WeatherResponse;
import com.example.weather.repository.WeatherEntityRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.groupingBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherResponseProduceService implements IWeatherResponseProduceService {
  @Autowired
  private WeatherEntityRepository weatherEntityRepository;

  @Autowired
  private EntityMapToWeatherResponseMapConverter entityMapToWeatherResponseMapConverter;
 
  @Override
  public Map<String, List<WeatherResponse>> getWeatherFromDbAndBuildResponseMap(LocalDate date) {
    List<WeatherEntity> rows = weatherEntityRepository.findByDateGreaterThanEqualOrderByIdDesc(date);

    Map<String, List<WeatherEntity>> groupedResults = rows.stream().collect(groupingBy(WeatherEntity::getLocation));

    Map<String, List<WeatherResponse>> map = entityMapToWeatherResponseMapConverter
      .buildWeatherResponseMapFromEntityMap(groupedResults);

    return map;
  }

}
