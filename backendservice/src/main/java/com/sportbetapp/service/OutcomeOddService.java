package com.sportbetapp.service;


import java.util.List;

import com.sportbetapp.domain.Outcome;
import com.sportbetapp.domain.OutcomeOdd;

public interface OutcomeOddService {
    List<OutcomeOdd> findAll();
    OutcomeOdd findById(Long id);
    OutcomeOdd save(OutcomeOdd outcomeOdd);

    void deleteAll();

    OutcomeOdd findByOutcome(Outcome outcome);
}