package com.epam.training.sportsbetting.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.epam.training.sportsbetting.domain.type.Currency;

import lombok.Data;

@Data
public class PlayerDto {
    private Long id;
    private String email;
    private String password;
    private String passwordConfirm;
    private String name;
    private Integer accountNumber;
    private BigDecimal balance;
    private Currency currency;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
}
