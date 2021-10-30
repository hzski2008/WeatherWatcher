package com.example.weather.jobscheduling;

import com.example.weather.service.IWeatherFetchService;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class WeatherFetchScheduler {
    
  private static final Logger LOGGER = LoggerFactory.getLogger(WeatherFetchScheduler.class);

  @Autowired
  private IWeatherFetchService fetchService;

  @Scheduled(initialDelayString = "${weather.inital_delay}", fixedDelayString = "${weather.interval}")
  @SchedulerLock(name = "scheduledTask")
  public void fetchWheather() {
    LOGGER.info("Fetch weather forecast from external resource at UTC time: " + LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
    fetchService.getWeatherForecastFromExternal();
  }
  
}
