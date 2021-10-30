package com.example.weather.service;

import com.example.weather.config.ConfigProperties;
import com.example.weather.model.OpenWeatherMapResponse;
import com.example.weather.model.converter.OpenWeatherMapResponseToEntityConverter;
import com.example.weather.model.WeatherEntity;
import com.example.weather.repository.WeatherEntityRepository;
import java.net.URI;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WeatherFetchService implements IWeatherFetchService {
  private static final Logger LOGGER = LoggerFactory.getLogger(WeatherFetchService.class);
  private final RestTemplate restTemplate;
  
  @Autowired
  private ConfigProperties config;

  @Autowired
  private OpenWeatherMapResponseToEntityConverter openWeatherMapResponseToEntityConverter;

  @Autowired
  private WeatherEntityRepository weatherEntityRepository;

  @Autowired
  public WeatherFetchService(RestTemplateBuilder restTemplateBuilder) {
    // set connection and read timeouts
    this.restTemplate = restTemplateBuilder.setConnectTimeout(Duration.ofSeconds(500))
      .setReadTimeout(Duration.ofSeconds(500)).build();
  }

  @Override
  public List<WeatherEntity> getWeatherForecastFromExternal() {
    List<OpenWeatherMapResponse> result = config.getLocations().stream().map(location -> {
      URI uri = buildLocationUri(location);    
      
      LOGGER.info("Getting weather forcasts in next 5 days for '" + location + "' from: " + uri);
      OpenWeatherMapResponse resp = restTemplate.getForObject(uri, OpenWeatherMapResponse.class);
      return resp;
    }).collect(Collectors.toList());

    // convert http response to entity
    List<WeatherEntity> weatherEntityList = openWeatherMapResponseToEntityConverter
        .buildWeatherEntityFromOpenWeatherMapResponse(result);

    // save to H2 database
    updateEntity(weatherEntityList);
    LOGGER.info("Save to db:" + weatherEntityList);
    return weatherEntityList;
  }

  private URI buildLocationUri(String location) {
    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(config.getUrl()).queryParam("q", location)
      .queryParam("units", config.getUnits()).queryParam("appid", config.getApi_key());
    URI uri = builder.buildAndExpand().toUri();

    return uri;
  }

  @Transactional
  public void updateEntity(List<WeatherEntity> weatherEntityList) {
    weatherEntityRepository.deleteAll();
    weatherEntityRepository.flush();
    weatherEntityRepository.saveAll(weatherEntityList);
  }
  
}
