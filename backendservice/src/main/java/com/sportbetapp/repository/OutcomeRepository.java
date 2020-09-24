package com.sportbetapp.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sportbetapp.domain.Bet;
import com.sportbetapp.domain.Outcome;


@Repository
public interface OutcomeRepository extends CrudRepository<Outcome, Long> {
    List<Outcome> findAllByBet(Bet bet);
}
