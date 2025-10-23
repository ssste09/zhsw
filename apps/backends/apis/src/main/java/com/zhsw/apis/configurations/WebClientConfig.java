package com.zhsw.apis.configurations;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient.Builder builder() {
        return WebClient.builder();
    }

    @Bean
    @Qualifier("geoWebClient")
    public WebClient geoWebClient(WebClient.Builder builder, @Value("${geo.open.meteo.base.url}") String baseUrl) {
        return builder.baseUrl(baseUrl).build();
    }

    @Bean
    @Qualifier("forecastWebClient")
    public WebClient forecastWebClient(
            WebClient.Builder builder, @Value("${forecast.open.meteo.base.url}") String baseUrl) {
        return builder.baseUrl(baseUrl).build();
    }
}
