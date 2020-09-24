package com.sportbetapp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sportbetapp.domain.Bet;
import com.sportbetapp.domain.SportEvent;
import com.sportbetapp.repository.BetRepository;
import com.sportbetapp.service.BetService;
import com.google.common.collect.Lists;


@Service
public class BetServiceImpl implements BetService {

    @Autowired
    private BetRepository betRepository;

    @Override
    public List<Bet> findAll() {
        return Lists.newArrayList(betRepository.findAll());
    }

    @Override
    public Bet findById(Long id) {
        return betRepository.findById(id).orElse(null);
    }

    @Override
    public Bet save(Bet bet) {
        return betRepository.save(bet);
    }

    @Override
    public List<Bet> findAllBySportEvent(SportEvent event) {
        return betRepository.findAllBySportEvent(event);
    }

    @Override
    public void deleteAll() {
        betRepository.deleteAll();
    }

}
