package com.sportbetapp.controller.mobile.rest.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class MobileUserDto {
    Long id;
    String email;
    String password;
    String name;
    BigDecimal balance;
    String currency;
    LocalDate birth;
    Integer numOfWagers;
}
