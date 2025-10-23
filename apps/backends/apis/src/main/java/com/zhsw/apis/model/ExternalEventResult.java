package com.zhsw.apis.model;

import lombok.Data;

import java.util.List;

@Data
public class ExternalEventResult {

    private List<ExternalEvent> events_results;
}
