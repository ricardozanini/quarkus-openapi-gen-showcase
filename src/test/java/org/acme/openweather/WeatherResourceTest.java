package org.acme.openweather;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;

@QuarkusTestResource(WireMockOpenWeatherServer.class)
@QuarkusTest
public class WeatherResourceTest {

    WireMockServer openWeatherServer;

    @ConfigProperty(name = "quarkus.openapi-generator.openweather_yaml.auth.app_id.api-key")
    String apiKey;

    @Test
    public void testGetWeatherByCityId() {
        final String cityId = "3163858";
        given().when().get("/weather/city/" + cityId).then().statusCode(200);
        openWeatherServer.verify(WireMock.getRequestedFor(
                WireMock.urlEqualTo("/data/2.5/weather?q=&id=" + cityId + "&lat=&lon=&zip=&units=&lang=&mode=&appid=" + apiKey)));
    }
}
