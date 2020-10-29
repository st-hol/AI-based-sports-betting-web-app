package com.sportbetapp.repository.betting;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sportbetapp.domain.betting.Bet;
import com.sportbetapp.domain.type.BetType;


@Repository
public interface BetRepository extends CrudRepository<Bet, Long> {
    Optional<Bet> findByType(BetType betType);
//    List<Bet> findAllBySportEvent(SportEvent event);
}
