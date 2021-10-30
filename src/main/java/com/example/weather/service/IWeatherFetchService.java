package com.example.weather.service;

import com.example.weather.model.WeatherEntity;
import java.util.List;

public interface IWeatherFetchService {
  public List<WeatherEntity> getWeatherForecastFromExternal();
}
