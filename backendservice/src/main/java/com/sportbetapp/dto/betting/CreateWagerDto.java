package com.sportbetapp.dto.betting;

import java.math.BigDecimal;

import com.sportbetapp.domain.betting.Outcome;
import com.sportbetapp.domain.type.Currency;

import lombok.Data;

@Data
public class CreateWagerDto {
    private Long betId;
    private Long sportEventId;
    private Outcome outcome;
    private Currency currency;
    private BigDecimal amount;
}
