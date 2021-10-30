package com.example.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.weather.model.WeatherEntity;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface WeatherEntityRepository extends JpaRepository<WeatherEntity, Long> {
    
  public List<WeatherEntity> findByLocationAndDateGreaterThanEqualOrderByIdDesc(String location, LocalDate date);
  public List<WeatherEntity> findByDateGreaterThanEqualOrderByIdDesc(LocalDate date);
  
}
