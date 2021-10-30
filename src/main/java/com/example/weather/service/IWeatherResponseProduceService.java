package com.example.weather.service;

import com.example.weather.model.WeatherResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IWeatherResponseProduceService {
  Map<String, List<WeatherResponse>> getWeatherFromDbAndBuildResponseMap(LocalDate date);
}
