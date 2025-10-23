package com.zhsw.apis.service;

import com.zhsw.apis.model.Coordinates;
import com.zhsw.apis.model.CoordinatesResult;
import com.zhsw.apis.model.ExternalWeatherResponse;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WeatherService {
    private final WebClient geoWebClient;
    private final WebClient forecastWebClient;

    public WeatherService(@Qualifier("geoWebClient") WebClient geoWebClient,
                          @Qualifier("forecastWebClient") WebClient forecastWebClient) {
        this.geoWebClient = geoWebClient;
        this.forecastWebClient = forecastWebClient;
    }

    public Coordinates getCoordinates(String cityName) {
        return Try.of(() -> geoWebClient
                        .get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/v1/search")
                                .queryParam("name", cityName)
                                .queryParam("count", 1)
                                .queryParam("language", "en")
                                .queryParam("format", "json")
                                .build())
                        .retrieve()
                        .bodyToMono(CoordinatesResult.class)
                        .block())
                .map(CoordinatesResult::getResults)
                .map(coordinates -> coordinates.get(0))
                .get();
    }

    public Try<ExternalWeatherResponse> getWeatherByCoordinates(Coordinates coordinates) {
        return Try.of(() -> forecastWebClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/forecast")
                        .queryParam("latitude", coordinates.getLatitude())
                        .queryParam("longitude", coordinates.getLongitude())
                        .queryParam(
                                "hourly",
                                "temperature_2m,relative_humidity_2m,precipitation_probability,precipitation,wind_speed_10m")
                        .build())
                .retrieve()
                .bodyToMono(ExternalWeatherResponse.class)
                .block());
    }
}
