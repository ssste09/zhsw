package com.zhsw.apis.configurations;

import com.zhsw.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Configuration
public class WebClientConfig extends SecurityConfig {

    @Bean
    public Map<HttpMethod, String[]> protectedRoutes() {
        return Map.of(HttpMethod.GET, new String[] {"/events/**", "/weather/**"});
    }

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
