package com.sportbetapp.service;


import java.util.List;

import com.sportbetapp.domain.Bet;
import com.sportbetapp.domain.Outcome;

public interface OutcomeService {
    List<Outcome> findAll();
    Outcome findById(Long id);
    Outcome save(Outcome outcome);

    void deleteAll();

    List<Outcome> findAllByBet(Bet bet);
}