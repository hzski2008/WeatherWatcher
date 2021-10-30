package com.example.weather.config;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "weather")
public class ConfigProperties {
  //@Value("#{'${weather.locations}'.split(',')}")  
  private List<String> locations;
  private int interval;
  private int inital_delay;
  private String units;
  private List<Double> thresholds;
  private String url;
  private String api_key;
}
