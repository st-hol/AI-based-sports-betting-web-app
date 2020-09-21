package com.epam.training.sportsbetting.service;


import java.util.List;

import com.epam.training.sportsbetting.domain.Wager;
import com.epam.training.sportsbetting.domain.user.User;
import com.epam.training.sportsbetting.exception.EventAlreadyStartedException;

public interface WagerService {
    List<Wager> findAll();
    Wager findById(Long id);
    Wager save(Wager wager);

    List<Wager> findAllByUser(User player);

    void deleteById(Long idWager) throws EventAlreadyStartedException;
}