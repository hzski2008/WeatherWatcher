package com.example.weather.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherMapResponse {

  @JsonProperty("list")
  private List<Forecast> forecasts;

  @JsonProperty("city")
  private City city;

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class City {
    @JsonProperty("name")
    private String name;
    @JsonProperty("country")
    private String country;
    @JsonProperty("timezone")
    private Integer timezone;
  }

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Forecast {
    @JsonProperty("dt")
    private Integer epochDate;
    @JsonProperty("main")
    private Temperature temperature;
    @JsonProperty("dt_txt")
    private String utcDate;
  }

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Temperature {
    @JsonProperty("temp")
    private Double temp;
    //@JsonProperty("temp_min")
    //private Double minimum;
    //@JsonProperty("temp_max")
    //private Double maximum;
  }

}
