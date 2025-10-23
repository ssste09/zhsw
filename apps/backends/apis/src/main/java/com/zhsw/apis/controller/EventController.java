package com.zhsw.apis.controller;

import com.zhsw.apis.mapper.EventMapper;
import com.zhsw.apis.service.EventService;
import org.openapitools.api.EventsApi;
import org.openapitools.model.Events;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController implements EventsApi {

    private final EventService eventService;
    private final EventMapper eventMapper;

    public EventController(EventService eventService, EventMapper eventMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
    }

    @Override
    public ResponseEntity<Events> eventsGET(String location) {
        return eventService
                .getEvents(location)
                .map(eventMapper::mapExternalEventResultToEvents)
                .map(ResponseEntity::ok)
                .get();
    }
}
