package com.zhsw.apis.mapper;

import com.zhsw.apis.model.ExternalEvent;
import com.zhsw.apis.model.ExternalEventResult;
import lombok.Data;
import org.openapitools.model.Event;
import org.openapitools.model.Events;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class EventMapper {

    public Event mapExternalEventToEvent(ExternalEvent externalEvent) {
        return new Event(
                externalEvent.getTitle(),
                externalEvent.getDate().getWhen(),
                mapAddressToString(externalEvent),
                externalEvent.getEvent_location_map().getImage(),
                externalEvent.getDescription());
    }

    private String mapAddressToString(ExternalEvent externalEvent) {
        return externalEvent.getAddress().get(0) + ", "
                + externalEvent.getAddress().get(1);
    }

    public Events mapExternalEventResultToEvents(ExternalEventResult externalEventResult) {
        List<Event> events = externalEventResult.getEvents_results().stream()
                .map(this::mapExternalEventToEvent)
                .toList();
        return new Events(events);
    }
}
