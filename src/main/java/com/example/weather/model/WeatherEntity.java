package com.example.weather.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "Weather")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeatherEntity implements Serializable {

  @Schema(description = "Unique identifier of the weather entity.", example = "1", required = true)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Schema(description = "City name", example = "Helsinki", required = true)
  @NotBlank(message = "Name is mandatory")
  private String location;

  @Schema(description = "Temperature", example = "11.55", required = true)
  private double temperature;

  @Schema(description = "Date", example = "2021-10-30", required = true)
  @NotNull(message = "Date must be provided")
  @JsonFormat(pattern="yyyy-MM-dd")
  private LocalDate date;

}
