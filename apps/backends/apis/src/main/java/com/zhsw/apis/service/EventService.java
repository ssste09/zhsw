package com.zhsw.apis.service;

import com.zhsw.apis.model.ExternalEventResult;
import io.vavr.concurrent.Future;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class EventService {

    private final WebClient webClient;

    @Value("${serp.api.key}")
    private String apiKey;

    public EventService(WebClient.Builder webClientBuilder, @Value("${serp.api.base.url}") String serpApiBaseUrl) {
        this.webClient = webClientBuilder.baseUrl(serpApiBaseUrl).build();
    }

    public Try<ExternalEventResult> getFilteredEvents(String location, String mood) {
        return Try.of(() -> webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search.json")
                        .queryParam("engine", "google_events")
                        .queryParam("q", "Events in " + location)
                        .queryParam("gl", "ch")
                        .queryParam("hl", "en")
                        .queryParam("api_key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(ExternalEventResult.class)
                .block());
    }

    public Try<ExternalEventResult> getFilteredRestaurants(String location, String mood) {
        return Try.of(() -> webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search.json")
                        .queryParam("engine", "google")
                        .queryParam("q", "Restaurants")
                        .queryParam("location", location)
                        .queryParam("gl", "ch")
                        .queryParam("hl", "en")
                        .queryParam("api_key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(ExternalEventResult.class)
                .block());
    }

    public Try<ExternalEventResult> getFilteredBars(String location, String mood) {
        return Try.of(() -> webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search.json")
                        .queryParam("engine", "google")
                        .queryParam("q", "Bars")
                        .queryParam("location", location)
                        .queryParam("gl", "ch")
                        .queryParam("hl", "en")
                        .queryParam("api_key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(ExternalEventResult.class)
                .block());
    }

    public Try<List<ExternalEventResult>> getFilteredResults(String location, String mood) {

        Try<ExternalEventResult> filteredEvents =
                Future.of(() -> getFilteredEvents(location, mood)).await().get();
        filteredEvents.get().setEventType("Events");

        Try<ExternalEventResult> filteredRestaurants =
                Future.of(() -> getFilteredRestaurants(location, mood)).await().get();
        filteredRestaurants.get().setEventType("Restaurants");

        Try<ExternalEventResult> filteredBars =
                Future.of(() -> getFilteredBars(location, mood)).await().get();
        filteredBars.get().setEventType("Bars");

        return Try.success(List.of(filteredEvents.get(), filteredRestaurants.get(), filteredBars.get()));
    }
}
