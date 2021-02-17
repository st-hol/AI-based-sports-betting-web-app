package com.sportbetapp.service.betting;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sportbetapp.domain.betting.Wager;
import com.sportbetapp.domain.user.User;
import com.sportbetapp.dto.betting.CreateWagerDto;
import com.sportbetapp.exception.EventAlreadyPredictedException;
import com.sportbetapp.exception.EventAlreadyStartedException;
import com.sportbetapp.exception.NotEnoughBalanceException;
import com.sportbetapp.exception.NotExistingGuessException;

public interface WagerService {
    List<Wager> findAll();
    Wager findById(Long id);
    Wager save(Wager wager);

    List<Wager> findAllByUser(User user);

    void deleteWager(Long idWager) throws EventAlreadyStartedException, EventAlreadyPredictedException;

    void createWagerWithGuess(CreateWagerDto wagerDto) throws NotEnoughBalanceException, NotExistingGuessException, EventAlreadyStartedException, EventAlreadyPredictedException;

    Page<Wager> findAllByUserPageable(User obtainCurrentPrincipleUser, Pageable pageable);

    int countWagersByUser(User currentUser);
}
