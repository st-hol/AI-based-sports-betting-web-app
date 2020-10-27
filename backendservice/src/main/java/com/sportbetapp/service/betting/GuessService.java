package com.sportbetapp.service.betting;


import java.util.List;

import com.sportbetapp.domain.betting.Bet;
import com.sportbetapp.domain.betting.SportEvent;
import com.sportbetapp.domain.betting.guess.Guess;

public interface GuessService {
    List<Guess> findAll();
    Guess findById(Long id);
    Guess save(Guess guess);

    void deleteAll();

    List<Guess> findAllByBet(Bet bet);

    List<Guess> findAllByEvent(SportEvent sportEvent);

}