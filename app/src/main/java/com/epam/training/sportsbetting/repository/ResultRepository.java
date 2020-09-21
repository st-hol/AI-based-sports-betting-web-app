package com.epam.training.sportsbetting.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.epam.training.sportsbetting.domain.Result;
import com.epam.training.sportsbetting.domain.SportEvent;


@Repository
public interface ResultRepository extends CrudRepository<Result, Long> {
    void deleteAllBySportEvent(SportEvent sportEvent);
}
