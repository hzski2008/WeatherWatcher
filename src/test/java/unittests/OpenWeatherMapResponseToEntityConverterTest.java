package unittests;

import com.example.weather.model.OpenWeatherMapResponse;
import com.example.weather.model.OpenWeatherMapResponse.Forecast;
import com.example.weather.model.OpenWeatherMapResponse.Temperature;
import com.example.weather.model.WeatherEntity;
import com.example.weather.model.converter.OpenWeatherMapResponseToEntityConverter;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import java.time.LocalDate;
import java.util.List;

@RunWith(SpringRunner.class)
public class OpenWeatherMapResponseToEntityConverterTest {

	private OpenWeatherMapResponseToEntityConverter converter;

	@Before
	public void init() {
	  converter = new OpenWeatherMapResponseToEntityConverter();
	}

	@Test
	public void test_toWeatherForecastEntityList() throws Exception {
          List<OpenWeatherMapResponse> respList = buildOpenWeatherMapResponseList();        
          List<WeatherEntity> results = converter.buildWeatherEntityFromOpenWeatherMapResponse(respList);
          
	  assertThat(results).extracting("location").contains("Helsinki");
	  assertThat(results).extracting("temperature").containsExactly(14.61);
	  assertThat(results).extracting("date").containsExactly(LocalDate.parse("2021-10-30"));
	}

	private List<OpenWeatherMapResponse> buildOpenWeatherMapResponseList() {         
            List<Forecast> list = List.of(
              new Forecast(1635595200, new Temperature(15.5), "2021-10-30 12:00:00"),
              new Forecast(1635606000, new Temperature(15.19), "2021-10-30 15:00:00"),
              new Forecast(1635616800, new Temperature(14.24), "2021-10-30 18:00:00"),
              new Forecast(1635627600, new Temperature(13.5), "2021-10-30 21:00:00")
            );
            
            var city = new OpenWeatherMapResponse.City("Helsinki", "FI", 10800);
            OpenWeatherMapResponse resp = new OpenWeatherMapResponse(list, city);
                      
            List<OpenWeatherMapResponse> respList = List.of(resp);
            return respList;
	}

}

