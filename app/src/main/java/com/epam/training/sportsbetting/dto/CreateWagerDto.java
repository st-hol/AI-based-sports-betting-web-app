package com.epam.training.sportsbetting.dto;

import java.math.BigDecimal;

import com.epam.training.sportsbetting.domain.Outcome;
import com.epam.training.sportsbetting.domain.type.Currency;

import lombok.Data;

@Data
public class CreateWagerDto {
    private Long betId;
    private Long sportEventId;
    private Outcome outcome;
    private Currency currency;
    private BigDecimal amount;
}
