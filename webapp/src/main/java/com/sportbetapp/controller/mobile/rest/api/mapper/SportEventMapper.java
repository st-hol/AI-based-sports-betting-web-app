package com.sportbetapp.controller.mobile.rest.api.mapper;

import com.sportbetapp.controller.mobile.rest.api.dto.MobileSportEventDto;
import com.sportbetapp.domain.betting.SportEvent;

public final class SportEventMapper {
    private SportEventMapper() {
    }

    public static MobileSportEventDto toSportEventMobileDto(SportEvent sportEvent) {
        if (sportEvent == null) {
            return null;
        }
        MobileSportEventDto dto = new MobileSportEventDto();
        dto.setId(sportEvent.getId());
        dto.setTitle(sportEvent.getTitle());
        dto.setSportType(sportEvent.getSportType().getValue());
        dto.setStartDate(sportEvent.getStartDate());
        dto.setEndDate(sportEvent.getEndDate());
        dto.setAlreadyPredicted(sportEvent.isAlreadyPredicted());
        return dto;
    }
}
