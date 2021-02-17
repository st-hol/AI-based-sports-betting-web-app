package com.sportbetapp.controller.mobile.rest.api.mapper;

import com.sportbetapp.controller.mobile.rest.api.dto.MakeBetMobileDto;
import com.sportbetapp.dto.betting.CreateWagerDto;

public final class MakeBetMapper {
    private MakeBetMapper() {
    }

    public static CreateWagerDto toMobileDto(MakeBetMobileDto makeBetMobileDto) {
        if (makeBetMobileDto == null) {
            return null;
        }
        CreateWagerDto dto = new CreateWagerDto();
        dto.setBetType(makeBetMobileDto.getBetType());
        dto.setSportEventId(makeBetMobileDto.getSportEventId());
        dto.setAmount(makeBetMobileDto.getAmount());
        dto.setSportType(makeBetMobileDto.getSportType());
        dto.setAwayTeamScore(makeBetMobileDto.getAwayTeamScore());
        dto.setHomeTeamScore(makeBetMobileDto.getHomeTeamScore());
        return dto;
    }
}
