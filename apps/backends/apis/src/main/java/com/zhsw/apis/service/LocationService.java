package com.zhsw.apis.service;

import com.zhsw.apis.model.ExternalLocation;
import com.zhsw.apis.model.ExternalLocationResult;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class LocationService {

    private final WebClient webClient;

    public LocationService(
            WebClient.Builder webClientBuilder, @Value("${open.plz.api.base.url}") String serpApiBaseUrl) {
        this.webClient = webClientBuilder.baseUrl(serpApiBaseUrl).build();
    }

    public Try<ExternalLocationResult> getLocation(String postalCode, String city) {
        var resultApi2 = Try.of(() -> webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/ch/Localities")
                        .queryParam("postalCode", postalCode)
                        .queryParam("name", city)
                        .build())
                .retrieve()
                .bodyToFlux(ExternalLocation.class)
                .collectList()
                .block());
        return Try.of(() -> {
            var result = new ExternalLocationResult();
            result.setLocations(resultApi2.get());
            return result;
        });
    }
}
