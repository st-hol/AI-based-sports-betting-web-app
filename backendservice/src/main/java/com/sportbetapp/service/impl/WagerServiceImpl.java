package com.sportbetapp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sportbetapp.domain.Wager;
import com.sportbetapp.domain.user.User;
import com.sportbetapp.exception.EventAlreadyStartedException;
import com.sportbetapp.repository.WagerRepository;
import com.sportbetapp.service.SportEventService;
import com.sportbetapp.service.WagerService;
import com.google.common.collect.Lists;


@Service
public class WagerServiceImpl implements WagerService {

    @Autowired
    private WagerRepository wagerRepository;
    @Autowired
    private SportEventService sportEventService;

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
    public void deleteById(Long idWager) throws EventAlreadyStartedException {
        if (eventAlreadyStarted(idWager)) {
            wagerRepository.deleteById(idWager);
        } else {
            throw new EventAlreadyStartedException("Oops! U are late :) ");
        }
    }

    private boolean eventAlreadyStarted(Long idWager) {
        return sportEventService.findByWager(findById(idWager)).isPresent();
    }

}
