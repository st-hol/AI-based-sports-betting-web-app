package com.epam.training.sportsbetting.service;


import com.epam.training.sportsbetting.domain.SportEvent;
import com.epam.training.sportsbetting.dto.BetDto;
import com.epam.training.sportsbetting.dto.OutcomeDto;
import com.epam.training.sportsbetting.dto.OutcomeOddDto;
import com.epam.training.sportsbetting.dto.ProcessResultDto;
import com.epam.training.sportsbetting.dto.SportEventDto;

public interface PopulateDataRestService {

    SportEventDto toSportEventDto(SportEvent sportEvent);

    SportEventDto populateSportEvent(SportEventDto sportEventDto);

    BetDto populateBetToSportEvent(BetDto betDto);

    OutcomeDto populateOutcomeToBet(OutcomeDto outcomeDto);

    OutcomeOddDto populateOutcomeOddToOutcome(OutcomeOddDto outcomeOddData);

    ProcessResultDto processResult(ProcessResultDto processResultDto);
}