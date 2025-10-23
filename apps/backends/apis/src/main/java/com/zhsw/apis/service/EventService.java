package com.zhsw.apis.service;

import com.zhsw.apis.model.ExternalEventResult;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class EventService {

    private final WebClient webClient;

    @Value("${serp.api.key}")
    private String apiKey;

    public EventService(WebClient.Builder webClientBuilder, @Value("${serp.api.base.url}") String serpApiBaseUrl) {
        this.webClient = webClientBuilder.baseUrl(serpApiBaseUrl).build();
    }

    public Try<ExternalEventResult> getEvents(String location) {
        return Try.of(() -> webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search.json")
                        .queryParam("q", "Events in " + location)
                        .queryParam("engine", "google_events")
                        .queryParam("gl", "ch")
                        .queryParam("hl", "en")
                        .queryParam("api_key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(ExternalEventResult.class)
                .block());
    }
}
