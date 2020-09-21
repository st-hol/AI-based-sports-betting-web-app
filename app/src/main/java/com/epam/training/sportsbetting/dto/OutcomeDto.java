package com.epam.training.sportsbetting.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OutcomeDto {
    private Long id;
    private Long betId;
    private String description;
    private List<OutcomeOddDto> outcomeOdds;
}
