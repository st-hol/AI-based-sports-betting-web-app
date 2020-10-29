package com.sportbetapp.repository.betting;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sportbetapp.domain.betting.Result;
import com.sportbetapp.domain.betting.SportEvent;


@Repository
public interface ResultRepository extends CrudRepository<Result, Long> {
    void deleteAllBySportEvent(SportEvent sportEvent);
}
