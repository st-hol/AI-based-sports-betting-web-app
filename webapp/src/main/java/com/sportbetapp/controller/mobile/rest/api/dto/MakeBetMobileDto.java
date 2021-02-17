package com.sportbetapp.controller.mobile.rest.api.dto;

import java.math.BigDecimal;

import com.sportbetapp.domain.type.BetType;

import lombok.Data;

@Data
public class MakeBetMobileDto {
    private BetType betType;
    private Long sportEventId;
    private BigDecimal amount;
    private String sportType;
    private Integer homeTeamScore;
    private Integer awayTeamScore;
}
