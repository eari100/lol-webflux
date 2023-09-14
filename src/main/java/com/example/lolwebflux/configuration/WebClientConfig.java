package com.example.lolwebflux.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${riot.api.key}")
    private String riotApiKey;

    @Bean
    public WebClient.Builder riotWebClientBuilder() {
        return WebClient.builder()
                .defaultHeader("X-Riot-Token", riotApiKey);
    }
}
