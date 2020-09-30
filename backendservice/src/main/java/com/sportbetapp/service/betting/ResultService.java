package com.sportbetapp.service.betting;


import java.util.List;

import com.sportbetapp.domain.betting.Result;
import com.sportbetapp.domain.betting.SportEvent;

public interface ResultService {
    List<Result> findAll();

    Result findById(Long id);

    Result save(Result result);

    void deleteAll();

    void deleteAllBySportEvent(SportEvent sportEvent);
}