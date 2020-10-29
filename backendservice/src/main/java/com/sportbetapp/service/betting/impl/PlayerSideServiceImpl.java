package com.sportbetapp.service.betting.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.sportbetapp.domain.betting.PlayerSide;
import com.sportbetapp.domain.type.SportType;
import com.sportbetapp.repository.betting.PlayerSideRepository;
import com.sportbetapp.service.betting.PlayerSideService;

@Service
public class PlayerSideServiceImpl implements PlayerSideService {

    @Autowired
    private PlayerSideRepository playerSideRepository;

    @Override
    public List<PlayerSide> findAll() {
        return Lists.newArrayList(playerSideRepository.findAll());
    }

    @Override
    public PlayerSide findById(PlayerSide.PlayerSideId playerSideId) {
        return playerSideRepository.findById(playerSideId
                                            ).orElse(null);
    }

    @Override
    public PlayerSide save(PlayerSide playerSide) {
        return playerSideRepository.save(playerSide);
    }

    @Override
    public PlayerSide findByNameAndSportType(String name, SportType sportType){
        return playerSideRepository.findByNameAndSportType(name, sportType);
    }

}
