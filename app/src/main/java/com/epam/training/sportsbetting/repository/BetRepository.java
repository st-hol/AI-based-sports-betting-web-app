package com.epam.training.sportsbetting.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.epam.training.sportsbetting.domain.Bet;
import com.epam.training.sportsbetting.domain.SportEvent;


@Repository
public interface BetRepository extends CrudRepository<Bet, Long> {
    List<Bet> findAllBySportEvent(SportEvent event);
}
