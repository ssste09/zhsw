package com.zhsw.apis.mapper;

import com.zhsw.apis.model.Date;
import com.zhsw.apis.model.EventLocationMap;
import com.zhsw.apis.model.ExternalEvent;
import com.zhsw.apis.model.ExternalEventResult;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openapitools.model.Event;
import org.openapitools.model.Events;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatObject;

@Tag("unit")
public class EventMapperTest {
    private final EventMapper eventMapper = new EventMapper();

    @Test
    void shouldMapExternalEventToEvent() {

        Date date = new Date();
        date.setWhen("Thu, 23 Oct, 20:00 – Fri, 24 Oct, 00:00");
        EventLocationMap locationMap = new EventLocationMap();
        locationMap.setImage("imageUrl");

        ExternalEvent externalEvent = new ExternalEvent();
        externalEvent.setTitle("Party");
        externalEvent.setDate(date);
        externalEvent.setAddress(List.of("Rutibohlstrasse 21, 8135", "Langnau"));
        externalEvent.setEvent_location_map(locationMap);
        externalEvent.setDescription("50s Party");

        var expected = new Event()
                .title("Party")
                .date(date.getWhen())
                .address("Rutibohlstrasse 21, 8135, Langnau")
                .locationImageUrl("imageUrl")
                .description("50s Party");

        var mapped = eventMapper.mapExternalEventToEvent(externalEvent);

        assertThatObject(mapped).isEqualTo(expected);
    }

    @Test
    void shouldMapExternalEventResultToEvents() {

        Date date = new Date();
        date.setWhen("Thu, 23 Oct, 20:00 – Fri, 24 Oct, 00:00");
        EventLocationMap locationMap = new EventLocationMap();
        locationMap.setImage("imageUrl");

        ExternalEvent externalEvent = new ExternalEvent();
        externalEvent.setTitle("Party");
        externalEvent.setDate(date);
        externalEvent.setAddress(List.of("Rutibohlstrasse 21, 8135", "Langnau"));
        externalEvent.setEvent_location_map(locationMap);
        externalEvent.setDescription("50s Party");

        Date date1 = new Date();
        date1.setWhen("Thu, 12 Nov, 22:00 – Sat, 12 Dec, 23:00");
        EventLocationMap locationMap1 = new EventLocationMap();
        locationMap1.setImage("imageUrl1");

        ExternalEvent externalEvent1 = new ExternalEvent();
        externalEvent1.setTitle("Concert");
        externalEvent1.setDate(date1);
        externalEvent1.setAddress(List.of("Brunaustrasse 25, 8137", "Zurich"));
        externalEvent1.setEvent_location_map(locationMap1);
        externalEvent1.setDescription("Ed Sheeran Concert");

        ExternalEventResult resultList = new ExternalEventResult();
        resultList.setEvents_results(List.of(externalEvent, externalEvent1));

        var expectedEvent = new Event()
                .title("Party")
                .date(date.getWhen())
                .address("Rutibohlstrasse 21, 8135, Langnau")
                .locationImageUrl("imageUrl")
                .description("50s Party");

        var expectedEvent1 = new Event()
                .title("Concert")
                .date(date1.getWhen())
                .address("Brunaustrasse 25, 8137, Zurich")
                .locationImageUrl("imageUrl1")
                .description("Ed Sheeran Concert");

        var expected = new Events().eventsList(List.of(expectedEvent, expectedEvent1));

        var mapped = eventMapper.mapExternalEventResultToEvents(resultList);

        assertThatObject(mapped).isEqualTo(expected);
    }
}
