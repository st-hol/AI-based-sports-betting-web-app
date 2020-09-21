package com.epam.training.sportsbetting.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.epam.training.sportsbetting.domain.Outcome;
import com.epam.training.sportsbetting.domain.OutcomeOdd;


@Repository
public interface OutcomeOddRepository extends CrudRepository<OutcomeOdd, Long> {
    OutcomeOdd findByOutcome(Outcome outcome);
}
