package com.sportbetapp.service;


import java.util.List;

import com.sportbetapp.domain.Bet;
import com.sportbetapp.domain.SportEvent;

public interface BetService {
    List<Bet> findAll();
    Bet findById(Long id);
    Bet save(Bet bet);

    List<Bet> findAllBySportEvent(SportEvent event);

    void deleteAll();
}