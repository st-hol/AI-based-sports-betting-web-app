package com.sportbetapp.service.betting;


import java.util.List;
import java.util.Optional;

import com.sportbetapp.domain.betting.SportEvent;
import com.sportbetapp.domain.betting.Wager;
import com.sportbetapp.domain.type.SportType;
import com.sportbetapp.dto.betting.CreateSportEventDto;

public interface SportEventService {
    List<SportEvent> findAll();
    SportEvent findById(Long id);
    SportEvent save(SportEvent sportEvent);

    void deleteAll();

    Optional<SportEvent> findByWager(Wager wager);

    List<SportType> findAllSportTypes();

    SportEvent createNewSportEvent(CreateSportEventDto createSportEventForm);
}