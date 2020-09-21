package com.epam.training.sportsbetting.service;


import java.util.List;

import com.epam.training.sportsbetting.domain.Bet;
import com.epam.training.sportsbetting.domain.SportEvent;

public interface BetService {
    List<Bet> findAll();
    Bet findById(Long id);
    Bet save(Bet bet);

    List<Bet> findAllBySportEvent(SportEvent event);

    void deleteAll();
}