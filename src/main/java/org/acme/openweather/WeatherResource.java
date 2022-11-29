package org.acme.openweather;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.openweathermap.api.CurrentWeatherDataApi;
import org.openweathermap.model.Model200;
import org.openweathermap.model.Weather;

@Produces(MediaType.APPLICATION_JSON)
@Path("/weather")
public class WeatherResource {

    @Inject
    @RestClient
    CurrentWeatherDataApi weatherDataApi;

    @GET
    @Path("/city/{id}")
    public List<Weather> getCityWeather(@PathParam("id") String cityId) {
        final Model200 response = weatherDataApi.currentWeatherData("", cityId, "", "", "", "", "", "");
        return response.getWeather();
    }

}
