package com.sportbetapp.controller.mobile.rest.api.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class MobileSportEventDto {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String title;
    private String sportType;
    private boolean alreadyPredicted;
}
