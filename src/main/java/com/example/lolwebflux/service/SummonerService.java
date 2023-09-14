package com.example.lolwebflux.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class SummonerService {

    private final WebClient.Builder riotWebClientBuilder;

    public Mono<String> getByName(String name) {
        final String URL = String.format("https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/%s", name);

        WebClient webClient = riotWebClientBuilder.baseUrl(URL)
                .build();

        return webClient.get()
                .uri(URL)
                .retrieve()
                .bodyToMono(String.class);
    }
}
