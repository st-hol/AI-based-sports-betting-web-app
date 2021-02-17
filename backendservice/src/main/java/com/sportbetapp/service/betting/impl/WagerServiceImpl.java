package com.sportbetapp.service.betting.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.sportbetapp.domain.betting.SportEvent;
import com.sportbetapp.domain.betting.Wager;
import com.sportbetapp.domain.betting.guess.Guess;
import com.sportbetapp.domain.type.OutcomeType;
import com.sportbetapp.domain.user.User;
import com.sportbetapp.dto.betting.CreateWagerDto;
import com.sportbetapp.exception.EventAlreadyPredictedException;
import com.sportbetapp.exception.EventAlreadyStartedException;
import com.sportbetapp.exception.NotEnoughBalanceException;
import com.sportbetapp.exception.NotExistingGuessException;
import com.sportbetapp.fm.GuessFactory;
import com.sportbetapp.repository.betting.WagerRepository;
import com.sportbetapp.service.betting.GuessService;
import com.sportbetapp.service.betting.SportEventService;
import com.sportbetapp.service.betting.WagerService;
import com.sportbetapp.service.user.UserService;
import com.sportbetapp.util.Utils;


@Service
public class WagerServiceImpl implements WagerService {

    @Autowired
    private WagerRepository wagerRepository;
    @Autowired
    private SportEventService sportEventService;
    @Autowired
    private UserService userService;
    @Autowired
    private GuessFactory guessFactory;
    @Autowired
    private GuessService guessService;
    @Autowired
    private WagerService wagerService;

    @Override
    public List<Wager> findAll() {
        return Lists.newArrayList(wagerRepository.findAll());
    }

    @Override
    public Wager findById(Long id) {
        return wagerRepository.findById(id).orElse(null);
    }

    @Override
    public Wager save(Wager wager) {
        return wagerRepository.save(wager);
    }

    @Override
    public List<Wager> findAllByUser(User user) {
        return wagerRepository.findAllByUser(user);
    }

    @Override
    @Transactional
    public void deleteWager(Long idWager) throws EventAlreadyStartedException, EventAlreadyPredictedException {
        if (eventAlreadyStarted(idWager)) {
            throw new EventAlreadyStartedException("Sorry, you can not delete the wager " +
                    "on event that already started or starts in one day.");
        }
        if (eventAlreadyPredicted(idWager)) {
            throw new EventAlreadyPredictedException("Sorry, you can not delete the wager " +
                    "on event that already predicted.");
        }
        Wager wager = wagerRepository.findById(idWager).orElseThrow(IllegalStateException::new);
        BigDecimal amountCompensation = wager.getAmount();
        wagerRepository.deleteById(idWager);
        User user = wager.getUser();
        userService.compensateBalance(user, amountCompensation);
    }

    ///user wager guess bet
    @Override
    @Transactional
    public void createWagerWithGuess(CreateWagerDto createWagerDto)
            throws NotEnoughBalanceException, NotExistingGuessException,
            EventAlreadyStartedException, EventAlreadyPredictedException {
        SportEvent sportEvent = sportEventService.findById(createWagerDto.getSportEventId());
        boolean alreadyStarted = Utils.isAfterOrEq(LocalDate.now(), sportEvent.getStartDate());
        if (alreadyStarted) {
            throw new EventAlreadyStartedException("Sorry, you can not make wager " +
                    "on event that already started or starts in one day.");
        }
        if (sportEvent.isAlreadyPredicted()) {
            throw new EventAlreadyPredictedException("Sorry, you can  not make wager" +
                    "on event that already predicted.");
        }
        User currentUser = userService.obtainCurrentPrincipleUser();
        Wager createdWager = populateWager(createWagerDto, currentUser);
        createGuess(createWagerDto, createdWager);
    }

    @Override
    public Page<Wager> findAllByUserPageable(User user, Pageable pageable) {
        return wagerRepository.findAllByUser(user, pageable);
    }

    @Override
    public int countWagersByUser(User currentUser) {

        return wagerRepository.countWagersByUser(currentUser);
    }

    private Wager populateWager(CreateWagerDto createWagerDto, User currentUser) throws NotEnoughBalanceException {
        final Wager wager = new Wager();
        wager.setUser(currentUser);
        BigDecimal wagerAmount = createWagerDto.getAmount();
        if (checkHasEnoughMoney(wagerAmount, currentUser)) {
            BigDecimal playerBalance = currentUser.getBalance();
            currentUser.setBalance(playerBalance.subtract(wagerAmount));
            userService.save(currentUser);
            wager.setAmount(wagerAmount);
            wager.setCreationTime(LocalDateTime.now());
            wager.setCurrency(currentUser.getCurrency());
            wager.setOutcomeType(OutcomeType.AWAITING);
            this.save(wager);
        } else {
            throw new NotEnoughBalanceException("Not enough money.", createWagerDto);
        }
        return wager;
    }

    private void createGuess(CreateWagerDto createWagerDto, Wager wager) throws NotExistingGuessException {
        Guess guess = guessFactory.makeWagerGuess(createWagerDto);
        guessService.save(guess);
        wager.setGuess(guess);
        wagerService.save(wager);
    }


    private boolean checkHasEnoughMoney(BigDecimal wagerAmount, User user) {
        return user.getBalance().compareTo(wagerAmount) >= 0;
    }

    private boolean eventAlreadyStarted(Long idWager) {
        Wager wager = findById(idWager);
        Guess guess = wager.getGuess();
        SportEvent sportEvent = guess.getSportEvent();
        return Utils.isAfterOrEq(LocalDate.now(), sportEvent.getStartDate());
    }


    private boolean eventAlreadyPredicted(Long idWager) {
        Wager wager = findById(idWager);
        Guess guess = wager.getGuess();
        SportEvent sportEvent = guess.getSportEvent();
        return sportEvent.isAlreadyPredicted() || !wager.getOutcomeType().equals(OutcomeType.AWAITING);
    }

}
