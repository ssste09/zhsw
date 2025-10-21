package com.zhsw.apis.model;

import lombok.Data;

@Data
public class ExternalWeatherResponse {

    private double elevation;
    private ExternalHourly hourly;
}
