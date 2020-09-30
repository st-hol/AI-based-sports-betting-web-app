package com.sportbetapp.repository.betting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sportbetapp.domain.betting.Bet;
import com.sportbetapp.domain.betting.SportEvent;


@Repository
public interface BetRepository extends CrudRepository<Bet, Long> {
    List<Bet> findAllBySportEvent(SportEvent event);
}
