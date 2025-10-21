package com.zhsw.apis.mapper;

import com.zhsw.apis.model.ExternalHourly;
import com.zhsw.apis.model.ExternalWeatherResponse;
import lombok.Data;
import org.openapitools.model.Hourly;
import org.openapitools.model.Weather;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
@Data
public class WeatherMapper {

    public List<Hourly> mapExternalHourlyToHourly(ExternalHourly externalHourly) {
        int size = externalHourly.getTime().size();
        return IntStream.range(0, size)
                .mapToObj(i -> {
                    Hourly h = new Hourly();
                    h.setTime(externalHourly.getTime().get(i));
                    h.setTemp(externalHourly.getTemperature_2m().get(i));
                    h.setHumidity(externalHourly.getRelative_humidity_2m().get(i));
                    h.setWind(externalHourly.getWind_speed_10m().get(i));
                    h.setPrecipitation(externalHourly.getPrecipitation().get(i));
                    return h;
                })
                .toList();
    }

    public Weather mapExternalWeatherResponseToWeather(ExternalWeatherResponse externalWeatherResponse) {

        List<Hourly> hourly = mapExternalHourlyToHourly(externalWeatherResponse.getHourly());

        return new Weather(externalWeatherResponse.getElevation(), hourly);
    }
}
