package com.sportbetapp.controller.mobile.rest.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MobileWagerDto {
    private Long id;
    private String username;
    private String betType;
    private String sportEventTitle;
    private String sportType;
    private LocalDateTime creationTime;
    private BigDecimal amount;
    private String currency;
    private String outcome;
}
