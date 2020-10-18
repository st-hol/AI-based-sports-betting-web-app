package com.sportbetapp.service.betting;


import java.util.List;

import com.sportbetapp.domain.betting.Bet;
import com.sportbetapp.domain.betting.PlayerSide;
import com.sportbetapp.domain.type.BetType;
import com.sportbetapp.domain.type.SportType;

public interface PlayerSideService {
    List<PlayerSide> findAll();

    PlayerSide findById(PlayerSide.PlayerSideId playerSideId);

    PlayerSide save(PlayerSide playerSide);

    PlayerSide findByNameAndSportType(String name, SportType sportType);

}