package com.example.lolwebflux.service;

import com.example.lolwebflux.dto.SummonerDto;
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
        Mono<SummonerDto> summonerMono = summonerService.getByName(summonerName);

        Flux<Object> matchResultsFlux = summonerMono
                .flatMapMany(summonerDto -> getMatchesIdsByPuuid(summonerDto.getPuuid(), 1, 20)
                        .flatMapMany(matchIds -> {
                            // matchIds를 사용하거나 출력합니다.
                            System.out.println("Match IDs: " + matchIds);

                            return Flux.fromIterable(matchIds) // matchIds 리스트를 Flux로 변환
                                    .flatMap(this::getMatchByMatchId); // 각 matchId에 대한 API 호출
                        }));

        return matchResultsFlux;
    }

    public Mono<List<String>> getMatchesIdsByPuuid(String puuid, int start, int count) {
        final String URL = String.format("https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/%s/ids?start=%d&count=%d", puuid, start, count);

        WebClient webClient = riotWebClientBuilder.baseUrl(URL)
                .build();

        return webClient.get()
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
