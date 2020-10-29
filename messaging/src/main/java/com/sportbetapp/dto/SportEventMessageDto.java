package com.sportbetapp.dto;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SportEventMessageDto implements Serializable {
    private static final long serialVersionUID = 300002228479017363L;
    private Long id;
    private LocalDate startDate;
    private String title;
    private String sportType;
    private boolean alreadyPredicted;
    private LocalDate endDate;

}
