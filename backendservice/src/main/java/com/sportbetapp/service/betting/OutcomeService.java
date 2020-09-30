package com.sportbetapp.service.betting;


import java.util.List;

import com.sportbetapp.domain.betting.Bet;
import com.sportbetapp.domain.betting.Outcome;

public interface OutcomeService {
    List<Outcome> findAll();
    Outcome findById(Long id);
    Outcome save(Outcome outcome);

    void deleteAll();

    List<Outcome> findAllByBet(Bet bet);
}