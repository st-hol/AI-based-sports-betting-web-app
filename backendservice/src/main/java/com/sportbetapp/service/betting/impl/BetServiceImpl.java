package com.sportbetapp.service.betting.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sportbetapp.domain.betting.Bet;
import com.sportbetapp.domain.betting.SportEvent;
import com.sportbetapp.domain.type.BetType;
import com.sportbetapp.repository.betting.BetRepository;
import com.sportbetapp.service.betting.BetService;
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

//    @Override
//    public List<Bet> findAllBySportEvent(SportEvent event) {
//        return betRepository.findAllBySportEvent(event);
//    }

    @Override
    public void deleteAll() {
        betRepository.deleteAll();
    }

    @Override
    public List<Bet> populateStandardBetsForNewSportEvent() {
        return Lists.newArrayList(betRepository.findAll());
    }

    @Override
    public Bet findByBetType(BetType betType) {
        return betRepository.findByType(betType).orElse(null);
    }

}
