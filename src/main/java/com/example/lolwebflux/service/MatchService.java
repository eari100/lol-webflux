package com.example.lolwebflux.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MatchService {

    private final WebClient.Builder riotWebClientBuilder;
    private final SummonerService summonerService;

    public Flux<Object> getMatchesBySummonerName(String summonerName) {
        return summonerService.getByName(summonerName)
                .flatMapMany(summonerDto -> getMatchesIdsByPuuid(summonerDto.getPuuid(), 0, 20)
                        .flatMapMany(matchIds -> Flux.fromIterable(matchIds)
                                .flatMapSequential(this::getMatchByMatchId)));
    }

    public Mono<List<String>> getMatchesIdsByPuuid(String puuid, int start, int count) {
        final String URL = String.format("https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/%s/ids?start=%d&count=%d", puuid, start, count);

        return riotWebClientBuilder.build()
                .get()
                .uri(URL)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {});
    }

    public Flux<Object> getMatchesByMatchId(List<String> matchIds) {
        return Flux.fromIterable(matchIds)
                .parallel()
                .runOn(Schedulers.parallel())
                .flatMap(this::getMatchByMatchId)
                .sequential();
    }

    private Mono<Object> getMatchByMatchId(String matchId) {
        final String URL = String.format("https://asia.api.riotgames.com/lol/match/v5/matches/%s", matchId);

        return riotWebClientBuilder.build()
                .get()
                .uri(URL)
                .retrieve()
                .bodyToMono(Object.class);
    }
}
