package org.acme;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.rest.client.reactive.ClientQueryParam;
import jakarta.ws.rs.GET;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestQuery;

@RegisterRestClient(baseUri = "https://api.open-meteo.com/v1/forecast")
public interface WeatherService {

    @GET
    @ClientQueryParam(name = "current_weather", value = "true")
    WeatherResponse getWeather(@RestQuery double latitude, @RestQuery double longitude);


    record WeatherResponse(@JsonProperty("current_weather") Weather weather) {
    }

    record Weather(double temperature, double windspeed) {
    }
}
