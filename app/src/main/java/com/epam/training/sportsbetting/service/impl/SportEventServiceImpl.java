package com.epam.training.sportsbetting.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.training.sportsbetting.domain.SportEvent;
import com.epam.training.sportsbetting.domain.Wager;
import com.epam.training.sportsbetting.repository.SportEventRepository;
import com.epam.training.sportsbetting.service.SportEventService;
import com.google.common.collect.Lists;


@Service

public class SportEventServiceImpl implements SportEventService {

    @Autowired
    private SportEventRepository sportEventRepository;

    @Override
    public List<SportEvent> findAll() {
        return Lists.newArrayList(sportEventRepository.findAll());
    }

    @Override
    public SportEvent findById(Long id) {
        return sportEventRepository.findById(id).orElse(null);
    }

    @Override
    public SportEvent save(SportEvent sportEvent) {
        return sportEventRepository.save(sportEvent);
    }

    @Override
    public void deleteAll() {
        sportEventRepository.deleteAll();
    }

    @Override
    public Optional<SportEvent> findByWager(Wager wager) {
        return sportEventRepository.findByWager(wager, LocalDateTime.now());
    }


}
