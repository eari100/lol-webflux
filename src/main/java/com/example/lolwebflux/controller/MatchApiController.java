package com.example.lolwebflux.controller;

import com.example.lolwebflux.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/match")
@RestController
public class MatchApiController {

    private final MatchService matchService;

    @GetMapping("/matches/by-puuid/{puuid}/ids")
    public Mono<List<String>> getMatchesIdsByPuuid(@PathVariable String puuid, @RequestParam(name = "start") int start, @RequestParam(name = "count") int count) {
        return matchService.getMatchesIdsByPuuid(puuid, start, count);
    }
}
