package com.zhsw.apis.mapper;

import com.zhsw.apis.model.ExternalHourly;
import com.zhsw.apis.model.ExternalWeatherResponse;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openapitools.model.Hourly;
import org.openapitools.model.Weather;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unit")
public class WeatherMapperTest {

    private final WeatherMapper mapper = new WeatherMapper();

    @Test
    void mapExternalHourlyToHourlyAndMapsAllFieldsByIndex() {
        ExternalHourly ext = new ExternalHourly();
        ext.setTime(List.of("2025-10-23T10:00Z", "2025-10-23T11:00Z"));
        ext.setTemperature_2m(List.of(12.3, 13.7));
        ext.setRelative_humidity_2m(List.of(60, 58));
        ext.setWind_speed_10m(List.of(4.2, 5.1));
        ext.setPrecipitation(List.of(0.0, 0.3));

        List<Hourly> mapped = mapper.mapExternalHourlyToHourly(ext);

        assertThat(mapped).hasSize(2);

        Hourly h0 = mapped.get(0);
        assertThat(h0.getTime()).isEqualTo("2025-10-23T10:00Z");
        assertThat(h0.getTemp()).isEqualTo(12.3);
        assertThat(h0.getHumidity()).isEqualTo(60);
        assertThat(h0.getWind()).isEqualTo(4.2);
        assertThat(h0.getPrecipitation()).isEqualTo(0.0);

        Hourly h1 = mapped.get(1);
        assertThat(h1.getTime()).isEqualTo("2025-10-23T11:00Z");
        assertThat(h1.getTemp()).isEqualTo(13.7);
        assertThat(h1.getHumidity()).isEqualTo(58);
        assertThat(h1.getWind()).isEqualTo(5.1);
        assertThat(h1.getPrecipitation()).isEqualTo(0.3);
    }

    @Test
    void mapExternalWeatherResponseToWeatherAndMapsElevationAndHourly() {
        ExternalHourly ext = new ExternalHourly();
        ext.setTime(List.of("t0"));
        ext.setTemperature_2m(List.of(10.5));
        ext.setRelative_humidity_2m(List.of(55));
        ext.setWind_speed_10m(List.of(4.0));
        ext.setPrecipitation(List.of(0.1));

        ExternalWeatherResponse response = new ExternalWeatherResponse();
        response.setElevation(408.0);
        response.setHourly(ext);

        Weather weather = mapper.mapExternalWeatherResponseToWeather(response);

        assertThat(weather.getElevation()).isEqualTo(408.0);
        assertThat(weather.getHourly()).hasSize(1);
        Hourly h = weather.getHourly().get(0);
        assertThat(h.getTime()).isEqualTo("t0");
        assertThat(h.getTemp()).isEqualTo(10.5);
        assertThat(h.getHumidity()).isEqualTo(55);
        assertThat(h.getWind()).isEqualTo(4.0);
        assertThat(h.getPrecipitation()).isEqualTo(0.1);
    }
}
