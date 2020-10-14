package com.sportbetapp.repository.betting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sportbetapp.domain.betting.Bet;
import com.sportbetapp.domain.betting.PlayerSide;
import com.sportbetapp.domain.betting.SportEvent;
import com.sportbetapp.domain.type.SportType;
import com.sportbetapp.dto.betting.PlayerSideDto;


@Repository
public interface PlayerSideRepository extends CrudRepository<PlayerSide, Long> {
    boolean existsByName(String name);

    PlayerSide findByNameAndSportType(String name, SportType sportType);

    List<PlayerSide> findAllBySportType(SportType sportType);
}
