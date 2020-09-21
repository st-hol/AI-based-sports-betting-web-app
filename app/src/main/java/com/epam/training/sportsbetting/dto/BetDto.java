package com.epam.training.sportsbetting.dto;

import java.util.List;

import com.epam.training.sportsbetting.domain.Bet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BetDto {
    private Long id;
    private String description;
    private List<OutcomeDto> outcomes;
    private Long sportEventId;
    private Bet.BetType type;
}
