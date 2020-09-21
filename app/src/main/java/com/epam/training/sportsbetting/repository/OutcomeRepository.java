package com.epam.training.sportsbetting.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.epam.training.sportsbetting.domain.Bet;
import com.epam.training.sportsbetting.domain.Outcome;


@Repository
public interface OutcomeRepository extends CrudRepository<Outcome, Long> {
    List<Outcome> findAllByBet(Bet bet);
}
