package com.sportbetapp.service;


import java.util.List;
import java.util.Optional;

import com.sportbetapp.domain.SportEvent;
import com.sportbetapp.domain.Wager;

public interface SportEventService {
    List<SportEvent> findAll();
    SportEvent findById(Long id);
    SportEvent save(SportEvent sportEvent);

    void deleteAll();

    Optional<SportEvent> findByWager(Wager wager);
}