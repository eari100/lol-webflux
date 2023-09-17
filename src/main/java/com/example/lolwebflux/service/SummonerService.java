package com.example.lolwebflux.service;

import com.example.lolwebflux.dto.SummonerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class SummonerService {

    private final WebClient.Builder riotWebClientBuilder;

    public Mono<SummonerDto> getByName(String name) {
        final String URL = String.format("https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/%s", name);

        return riotWebClientBuilder.build()
                .get()
                .uri(URL)
                .retrieve()
                .bodyToMono(SummonerDto.class);
    }
}
