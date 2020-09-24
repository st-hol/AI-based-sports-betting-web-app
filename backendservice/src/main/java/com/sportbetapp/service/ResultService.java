package com.sportbetapp.service;


import java.util.List;

import com.sportbetapp.domain.Result;
import com.sportbetapp.domain.SportEvent;

public interface ResultService {
    List<Result> findAll();

    Result findById(Long id);

    Result save(Result result);

    void deleteAll();

    void deleteAllBySportEvent(SportEvent sportEvent);
}