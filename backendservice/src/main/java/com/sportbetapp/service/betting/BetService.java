package com.sportbetapp.service.betting;


import java.util.List;

import com.sportbetapp.domain.betting.Bet;
import com.sportbetapp.domain.betting.SportEvent;
import com.sportbetapp.domain.type.BetType;

public interface BetService {
    List<Bet> findAll();
    Bet findById(Long id);
    Bet save(Bet bet);

//    List<Bet> findAllBySportEvent(SportEvent event);

    void deleteAll();

    List<Bet> populateStandardBetsForNewSportEvent();

    Bet findByBetType(BetType betType);
}