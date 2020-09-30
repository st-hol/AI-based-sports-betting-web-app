package com.sportbetapp.service.betting;


import java.util.List;

import com.sportbetapp.domain.betting.Bet;
import com.sportbetapp.domain.betting.SportEvent;

public interface BetService {
    List<Bet> findAll();
    Bet findById(Long id);
    Bet save(Bet bet);

    List<Bet> findAllBySportEvent(SportEvent event);

    void deleteAll();
}