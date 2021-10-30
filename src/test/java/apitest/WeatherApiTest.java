package apitest;

import com.example.weather.model.WeatherResponse;
import com.jayway.restassured.RestAssured;
import static com.jayway.restassured.RestAssured.given;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import org.junit.BeforeClass;
import org.junit.Test;

public class WeatherApiTest {

  @BeforeClass
  public static void setup() {
    RestAssured.reset();
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = 8080;
    System.out.println("Accessing " + RestAssured.baseURI + " - Port:" + RestAssured.port);
  }

  @Test
  public void testGetWeatherOkCase() {
    given().when().get("/api/weather-in-5days").then().statusCode(200).body("Helsinki", notNullValue());
  }

  @Test
  public void testGetWeatherWithParameterOkCase() {
    given().when().get("/api/weather-in-5days?city=Helsinki").then().statusCode(200).body("Helsinki", notNullValue());
  }

  @Test
  public void testNotFoundError() {
    given().when().get("/api/weather-in-5days?city=Hhhh").then().statusCode(404).body("error", equalTo("Not Found"));
  }

  private Map<String, List<WeatherResponse>> getWeather(String city) {
    Response res = given().when().get("/api/weather-in-5days?city=" + city).then().statusCode(200).extract().response();

    JsonPath jsonPathEvaluator = res.jsonPath();
    Map<String, List<WeatherResponse>> result = jsonPathEvaluator.get();
    return result;
  }

  @Test
  public void testGetWeatherWithParalleExecution() throws Exception {
    ExecutorService executor = Executors.newCachedThreadPool();
    List<Callable<Map<String, List<WeatherResponse>>>> callables = Arrays.asList(() -> getWeather("Helsinki"),
        () -> getWeather("London"), () -> getWeather("Helsinki"), () -> getWeather("Beijing"));

    List<Future<Map<String, List<WeatherResponse>>>> resultsFuture = executor.invokeAll(callables);
    List<Map<String, List<WeatherResponse>>> results = new ArrayList<>();
    for (Future<Map<String, List<WeatherResponse>>> map : resultsFuture) {
      results.add(map.get());
    }

    executor.shutdown();

    assertThat(results, hasSize(4));
    List<String> expected = new ArrayList<>(Arrays.asList("Helsinki", "Helsinki", "London", "Beijing"));
    results.stream().forEach(actual -> {
      String key = (String) actual.keySet().toArray()[0];
      assertThat(expected, hasItems(key));
      expected.remove(key); // remove checked item from expected list
    });
  }
  
  // other tests...
}
