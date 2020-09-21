package com.epam.training.sportsbetting.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OutcomeOddDto {
    private Long id;
    private BigDecimal value;
    private Long outcomeId;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
}
