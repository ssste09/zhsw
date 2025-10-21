package com.zhsw.apis.model;

import lombok.Data;

import java.util.List;

@Data
public class ExternalHourly {

    private List<String> time;
    private List<Double> temperature_2m;
    private List<Integer> relative_humidity_2m;
    private List<Integer> precipitation_probability;
    private List<Double> precipitation;
    private List<Double> wind_speed_10m;
}
