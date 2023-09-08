package org.acme;

import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/")
public class TheBestPlaceToBeResource {

    @RestClient
    WeatherService service;

    static final double VALENCE_LATITUDE = 44.9;
    static final double VALENCE_LONGITUDE = 4.9;

    static final double ATHENS_LATITUDE = 37.9;
    static final double ATHENS_LONGITUDE = 23.7;

    @GET
    @RunOnVirtualThread
    public String getTheBestPlaceToBe() {
        var valence = service.getWeather(VALENCE_LATITUDE, VALENCE_LONGITUDE).weather().temperature();
        var athens = service.getWeather(ATHENS_LATITUDE, ATHENS_LONGITUDE).weather().temperature();


        // Advanced decision tree
        if (valence > athens && valence <= 35) {
            return "Valence! (" + Thread.currentThread() + ")";
        } else if (athens > 35) {
            return "Valence! (" + Thread.currentThread() + ")";
        } else {
            return "Athens (" + Thread.currentThread() + ")";
        }
    }

}
