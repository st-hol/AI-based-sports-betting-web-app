package com.sportbetapp.service.betting;


import java.util.List;

import com.sportbetapp.domain.betting.Wager;
import com.sportbetapp.domain.user.User;
import com.sportbetapp.exception.EventAlreadyStartedException;

public interface WagerService {
    List<Wager> findAll();
    Wager findById(Long id);
    Wager save(Wager wager);

    List<Wager> findAllByUser(User user);

    void deleteById(Long idWager) throws EventAlreadyStartedException;
}