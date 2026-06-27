package com.zhsw.apis.controller;

import com.zhsw.apis.mapper.EventMapper;
import com.zhsw.apis.service.EventService;
import org.openapitools.api.EventsApi;
import org.openapitools.model.EventGroup;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EventController implements EventsApi {

    private final EventService eventService;
    private final EventMapper eventMapper;

    public EventController(EventService eventService, EventMapper eventMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
    }

    @Override
    public ResponseEntity<List<EventGroup>> eventsGET(String location, String mood) {
        return eventService
                .getFilteredResults(location, mood)
                .map(results -> results.stream()
                        .map(eventMapper::mapExternalEventResultToEvents)
                        .toList())
                .map(ResponseEntity::ok)
                .get();
    }
}
