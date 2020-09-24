package com.sportbetapp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sportbetapp.domain.Bet;
import com.sportbetapp.domain.Outcome;
import com.sportbetapp.repository.OutcomeRepository;
import com.sportbetapp.service.OutcomeService;
import com.google.common.collect.Lists;


@Service
public class OutcomeServiceImpl implements OutcomeService {

    @Autowired
    private OutcomeRepository outcomeRepository;

    @Override
    public List<Outcome> findAll() {
        return Lists.newArrayList(outcomeRepository.findAll());
    }

    @Override
    public Outcome findById(Long id) {
        return outcomeRepository.findById(id).orElse(null);
    }

    @Override
    public Outcome save(Outcome outcome) {
        return outcomeRepository.save(outcome);
    }

    @Override
    public void deleteAll() {
        outcomeRepository.deleteAll();
    }

    @Override
    public List<Outcome> findAllByBet(Bet bet) {
        return outcomeRepository.findAllByBet(bet);
    }

}
