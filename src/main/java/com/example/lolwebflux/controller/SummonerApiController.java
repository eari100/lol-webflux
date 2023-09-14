package com.example.lolwebflux.controller;

import com.example.lolwebflux.service.SummonerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RequestMapping("/api/summoner")
@RestController
public class SummonerApiController {

    private final SummonerService summonerService;

    @GetMapping("/{name}")
    public Mono<String> getByName(@PathVariable String name) {
        return summonerService.getByName(name);
    }
}
