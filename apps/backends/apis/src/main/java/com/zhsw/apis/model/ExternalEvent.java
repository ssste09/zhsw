package com.zhsw.apis.model;

import lombok.Data;

import java.util.List;

@Data
public class ExternalEvent {
    private String title;
    private Date date;
    private List<String> address;
    private EventLocationMap event_location_map;
    private String description;
}
