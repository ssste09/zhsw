package com.zhsw.apis.service;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Tag("integration")
public class EventServiceTest {

    @Autowired
    EventService eventService;

    @InjectMocks
    static MockWebServer mockWebServer = new MockWebServer();

    @BeforeAll
    static void start() throws Exception {
        mockWebServer.start();
    }

    @AfterAll
    static void stop() throws Exception {
        mockWebServer.shutdown();
    }

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("serp.api.base.url", () -> mockWebServer.url("/").toString());
        r.add("serp.api.key", () -> "test-key");
    }

    @Test
    void getEvents() throws Exception {
        mockWebServer.enqueue(
                new MockResponse()
                        .setHeader("Content-Type", "application/json")
                        .setBody(
                                """
          {"events_results":[{"title":"Concert",
          "date": {"when":"25 Oct, 22:31"},"address":["Zurcherstrasse 21, 3108", "Zurich"],
          "event_location_map":{"image":"imageUrl"},"description":"Ed Sheeran concert"}, {"title":"Party",
          "date": {"when":"28 Oct, 18:00"},"address":["Brunaustrasse 73, 2356", "Zurich"],
          "event_location_map":{"image":"url"},"description":"50s party"}]}
        """));

        var c = eventService.getEvents("Zurich");

        var req = mockWebServer.takeRequest();
        String decoded = URLDecoder.decode(req.getPath(), StandardCharsets.UTF_8);

        assertThat(req.getMethod()).isEqualTo("GET");
        assertThat(req.getPath()).startsWith("/search.json");

        assertThat(decoded)
                .contains("q=Events in Zurich")
                .contains("engine=google_events")
                .contains("gl=ch")
                .contains("hl=en")
                .contains("api_key=test-key");

        assertThat(c.get().getEvents_results().get(0).getAddress().get(0)).isEqualTo("Zurcherstrasse 21, 3108");
        assertThat(c.get().getEvents_results().get(1).getEvent_location_map().getImage())
                .isEqualTo("url");
    }
}
