package com.example.lolwebflux.dto;

import lombok.Getter;

@Getter
public class SummonerDto {
    private String id;
    private String accountId;
    private int profileIconId;
    private long revisionDate;
    private String name;
    private String puuid;
    private long summonerLevel;
}
