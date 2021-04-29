package com.sportbetapp.dto.betting;

import java.math.BigDecimal;

import com.sportbetapp.domain.type.Currency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointsTurnoverStatisticDto {
    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private BigDecimal wastedAll;
    private BigDecimal wonAll;
    private BigDecimal difference;
    private Currency currency;
    private Boolean isProfitable;
}
