package com.sportbetapp.service.betting.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sportbetapp.domain.betting.Result;
import com.sportbetapp.domain.betting.SportEvent;
import com.sportbetapp.repository.betting.ResultRepository;
import com.sportbetapp.service.betting.ResultService;
import com.google.common.collect.Lists;

@Service
public class ResultServiceImpl implements ResultService {

    @Autowired
    private ResultRepository resultRepository;

    @Override
    public List<Result> findAll() {
        return Lists.newArrayList(resultRepository.findAll());
    }

    @Override
    public Result findById(Long id) {
        return resultRepository.findById(id).orElse(null);
    }

    @Override
    public Result save(Result result) {
        return resultRepository.save(result);
    }

    @Override
    public void deleteAll() {
        resultRepository.deleteAll();
    }

    @Override
    public void deleteAllBySportEvent(SportEvent sportEvent) {
        resultRepository.deleteAllBySportEvent(sportEvent);
    }


}
