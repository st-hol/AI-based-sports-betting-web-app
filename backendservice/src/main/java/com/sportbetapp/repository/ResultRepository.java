package com.sportbetapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sportbetapp.domain.Result;
import com.sportbetapp.domain.SportEvent;


@Repository
public interface ResultRepository extends CrudRepository<Result, Long> {
    void deleteAllBySportEvent(SportEvent sportEvent);
}
