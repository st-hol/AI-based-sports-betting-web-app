package com.sportbetapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sportbetapp.domain.Outcome;
import com.sportbetapp.domain.OutcomeOdd;


@Repository
public interface OutcomeOddRepository extends CrudRepository<OutcomeOdd, Long> {
    OutcomeOdd findByOutcome(Outcome outcome);
}
