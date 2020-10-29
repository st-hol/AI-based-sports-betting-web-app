package com.sportbetapp.repository.betting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sportbetapp.domain.betting.Bet;
import com.sportbetapp.domain.betting.SportEvent;
import com.sportbetapp.domain.betting.guess.Guess;


@Repository
public interface GuessRepository extends CrudRepository<Guess, Long> {
    List<Guess> findAllByBet(Bet bet);

    List<Guess> findAllBySportEvent(SportEvent sportEvent);
}
