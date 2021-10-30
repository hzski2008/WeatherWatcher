package com.example.weather.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeatherResponse {
  
  private LocalDate date;
  private Double tempature;
  private String units;
  private Double threshold;
  private Boolean limitExceed;
  
}
