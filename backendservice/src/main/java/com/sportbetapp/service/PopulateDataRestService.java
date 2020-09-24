package com.sportbetapp.service;


import com.sportbetapp.domain.SportEvent;
import com.sportbetapp.dto.BetDto;
import com.sportbetapp.dto.OutcomeDto;
import com.sportbetapp.dto.OutcomeOddDto;
import com.sportbetapp.dto.ProcessResultDto;
import com.sportbetapp.dto.SportEventDto;

public interface PopulateDataRestService {

    SportEventDto toSportEventDto(SportEvent sportEvent);

    SportEventDto populateSportEvent(SportEventDto sportEventDto);

    BetDto populateBetToSportEvent(BetDto betDto);

    OutcomeDto populateOutcomeToBet(OutcomeDto outcomeDto);

    OutcomeOddDto populateOutcomeOddToOutcome(OutcomeOddDto outcomeOddData);

    ProcessResultDto processResult(ProcessResultDto processResultDto);
}