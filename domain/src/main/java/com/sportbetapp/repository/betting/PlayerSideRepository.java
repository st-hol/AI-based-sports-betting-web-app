package com.sportbetapp.repository.betting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sportbetapp.domain.betting.PlayerSide;
import com.sportbetapp.domain.type.SportType;


@Repository
public interface PlayerSideRepository extends CrudRepository<PlayerSide, PlayerSide.PlayerSideId> {
    boolean existsByName(String name);

    PlayerSide findByNameAndSportType(String name, SportType sportType);

    List<PlayerSide> findAllBySportType(SportType sportType);
}
