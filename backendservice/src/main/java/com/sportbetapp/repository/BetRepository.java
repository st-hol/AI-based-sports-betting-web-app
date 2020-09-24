package com.sportbetapp.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sportbetapp.domain.Bet;
import com.sportbetapp.domain.SportEvent;


@Repository
public interface BetRepository extends CrudRepository<Bet, Long> {
    List<Bet> findAllBySportEvent(SportEvent event);
}
