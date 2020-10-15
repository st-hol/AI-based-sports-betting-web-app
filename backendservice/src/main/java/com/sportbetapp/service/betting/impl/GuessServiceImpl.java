package com.sportbetapp.service.betting.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sportbetapp.domain.betting.Bet;
import com.sportbetapp.domain.betting.guess.Guess;
import com.sportbetapp.repository.betting.GuessRepository;
import com.sportbetapp.service.betting.GuessService;
import com.google.common.collect.Lists;


@Service
public class GuessServiceImpl implements GuessService {

    @Autowired
    private GuessRepository guessRepository;

    @Override
    public List<Guess> findAll() {
        return Lists.newArrayList(guessRepository.findAll());
    }

    @Override
    public Guess findById(Long id) {
        return guessRepository.findById(id).orElse(null);
    }

    @Override
    public Guess save(Guess guess) {
        return guessRepository.save(guess);
    }

    @Override
    public void deleteAll() {
        guessRepository.deleteAll();
    }

    @Override
    public List<Guess> findAllByBet(Bet bet) {
        return guessRepository.findAllByBet(bet);
    }

}
