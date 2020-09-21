package com.epam.training.sportsbetting.service;


import java.util.List;

import com.epam.training.sportsbetting.domain.Bet;
import com.epam.training.sportsbetting.domain.Outcome;

public interface OutcomeService {
    List<Outcome> findAll();
    Outcome findById(Long id);
    Outcome save(Outcome outcome);

    void deleteAll();

    List<Outcome> findAllByBet(Bet bet);
}