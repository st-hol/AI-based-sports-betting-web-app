package com.epam.training.sportsbetting.service;


import java.util.List;

import com.epam.training.sportsbetting.domain.Outcome;
import com.epam.training.sportsbetting.domain.OutcomeOdd;

public interface OutcomeOddService {
    List<OutcomeOdd> findAll();
    OutcomeOdd findById(Long id);
    OutcomeOdd save(OutcomeOdd outcomeOdd);

    void deleteAll();

    OutcomeOdd findByOutcome(Outcome outcome);
}