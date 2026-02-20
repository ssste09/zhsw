package com.zhsw.apis.controller;

import com.zhsw.apis.mapper.WeatherMapper;
import com.zhsw.apis.service.WeatherService;
import org.openapitools.api.WeatherApi;
import org.openapitools.model.Weather;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController implements WeatherApi {
    private final WeatherService weatherService;
    private final WeatherMapper weatherMapper;

    public WeatherController(WeatherService weatherService, WeatherMapper weatherMapper) {
        this.weatherService = weatherService;
        this.weatherMapper = weatherMapper;
    }

    @PreAuthorize("isAuthenticated()")
    @Override
    public ResponseEntity<Weather> weatherGET(String cityName) {
        var coordinates = weatherService.getCoordinates(cityName);
        return weatherService
                .getWeatherByCoordinates(coordinates)
                .map(weatherMapper::mapExternalWeatherResponseToWeather)
                .map(ResponseEntity::ok)
                .get();
    }
}
