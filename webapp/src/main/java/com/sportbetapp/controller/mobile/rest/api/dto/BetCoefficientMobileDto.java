package com.sportbetapp.controller.mobile.rest.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BetCoefficientMobileDto {
    private String name;
    private double coefficient;
}
