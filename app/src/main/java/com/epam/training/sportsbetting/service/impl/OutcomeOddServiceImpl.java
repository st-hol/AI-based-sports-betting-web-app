package com.epam.training.sportsbetting.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.training.sportsbetting.domain.Outcome;
import com.epam.training.sportsbetting.domain.OutcomeOdd;
import com.epam.training.sportsbetting.repository.OutcomeOddRepository;
import com.epam.training.sportsbetting.service.OutcomeOddService;
import com.google.common.collect.Lists;


@Service
public class OutcomeOddServiceImpl implements OutcomeOddService {

    @Autowired
    private OutcomeOddRepository outcomeOddRepository;

    @Override
    public List<OutcomeOdd> findAll() {
        return Lists.newArrayList(outcomeOddRepository.findAll());
    }

    @Override
    public OutcomeOdd findById(Long id) {
        return outcomeOddRepository.findById(id).orElse(null);
    }

    @Override
    public OutcomeOdd save(OutcomeOdd outcomeOdd) {
        return outcomeOddRepository.save(outcomeOdd);
    }

    @Override
    public void deleteAll() {
        outcomeOddRepository.deleteAll();
    }

    @Override
    public OutcomeOdd findByOutcome(Outcome outcome) {
        return outcomeOddRepository.findByOutcome(outcome);
    }

}
