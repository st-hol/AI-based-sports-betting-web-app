package com.epam.training.sportsbetting.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SportEventDto {
    private Long id;
    private LocalDateTime startDate;
    private String title;
    private LocalDateTime endDate;
    private List<BetDto> bets;
    private EventType eventType;

    public enum EventType {
        FOOTBALL_EVENT, TENNIS_EVENT
    }
}
