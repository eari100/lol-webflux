package com.example.lolwebflux.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MatchService {

    private final WebClient.Builder riotWebClientBuilder;

    public Mono<List<String>> getMatchesIdsByPuuid(String puuid, int start, int count) {
        final String URL = String.format("https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/%s/ids?start=%d&count=%d", puuid, start, count);

        WebClient webClient = riotWebClientBuilder.baseUrl(URL)
                .build();

        return webClient.get()
                .uri(URL)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {});
    }
}
