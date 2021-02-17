package com.sportbetapp.controller.mobile.rest.api.mapper;

import com.sportbetapp.controller.mobile.rest.api.dto.MobileWagerDto;
import com.sportbetapp.domain.betting.Wager;

public final class WagerMapper {
    private WagerMapper() {
    }

    public static MobileWagerDto toMobileWagerDto(Wager wager) {
        if (wager == null) {
            return null;
        }

        MobileWagerDto dto = new MobileWagerDto();
        dto.setId(wager.getId());
        dto.setUsername(wager.getUser().getEmail());
        dto.setBetType(wager.getGuess().getBet().getType().getValue());
        dto.setSportType(wager.getGuess().getSportEvent().getSportType().getValue());
        dto.setSportEventTitle(wager.getGuess().getSportEvent().getTitle());
        dto.setAmount(wager.getAmount());
        dto.setCreationTime(wager.getCreationTime());
        dto.setCurrency(wager.getCurrency().getValue());
        dto.setOutcome(wager.getOutcomeType().getValue());
        return dto;
    }
}
