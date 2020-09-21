package com.epam.training.sportsbetting.service;


import java.util.List;
import java.util.Optional;

import com.epam.training.sportsbetting.domain.SportEvent;
import com.epam.training.sportsbetting.domain.Wager;

public interface SportEventService {
    List<SportEvent> findAll();
    SportEvent findById(Long id);
    SportEvent save(SportEvent sportEvent);

    void deleteAll();

    Optional<SportEvent> findByWager(Wager wager);
}