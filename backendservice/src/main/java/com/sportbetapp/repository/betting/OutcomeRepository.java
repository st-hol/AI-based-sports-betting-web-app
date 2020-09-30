package com.sportbetapp.repository.betting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sportbetapp.domain.betting.Bet;
import com.sportbetapp.domain.betting.Outcome;


@Repository
public interface OutcomeRepository extends CrudRepository<Outcome, Long> {
    List<Outcome> findAllByBet(Bet bet);
}
