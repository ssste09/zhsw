package com.zhsw.apis.service;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Tag("integration")
public class WeatherServiceTest {
    static MockWebServer geo = new MockWebServer();
    static MockWebServer forecast = new MockWebServer();

    @BeforeAll
    static void start() throws Exception {
        geo.start();
        forecast.start();
    }

    @AfterAll
    static void stop() throws Exception {
        geo.shutdown();
        forecast.shutdown();
    }

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("geo.open.meteo.base.url", () -> geo.url("/").toString());
        r.add("forecast.open.meteo.base.url", () -> forecast.url("/").toString());
    }

    @Autowired
    WeatherService service;

    @Test
    void getCoordinates_happyPath() throws Exception {
        geo.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("""
          {"results":[{"latitude":47.3769,"longitude":8.5417}]}
        """));

        var c = service.getCoordinates("Zurich");
        assertThat(c.getLatitude()).isEqualTo(47.3769);
        assertThat(c.getLongitude()).isEqualTo(8.5417);
    }
}
