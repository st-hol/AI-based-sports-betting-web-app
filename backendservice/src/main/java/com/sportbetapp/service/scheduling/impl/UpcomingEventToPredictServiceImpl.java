package com.sportbetapp.service.scheduling.impl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.sportbetapp.domain.technical.messaging.UpcomingEventToPredict;
import com.sportbetapp.repository.technical.messaging.UpcomingEventToPredictRepository;
import com.sportbetapp.service.scheduling.UpcomingEventToPredictService;

@Service
public class UpcomingEventToPredictServiceImpl implements UpcomingEventToPredictService {

    private static final int LIMIT_OF_TOP_UPCOMING = 3;


    @Autowired
    private UpcomingEventToPredictRepository repository;

    @Override
    public List<UpcomingEventToPredict> findAll() {
        return Lists.newArrayList(repository.findAll());
    }

    @Override
    public UpcomingEventToPredict findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public UpcomingEventToPredict save(UpcomingEventToPredict upcomingEventToPredict) {
        return repository.save(upcomingEventToPredict);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public List<UpcomingEventToPredict> findTop3Upcoming() {
        return findAll().stream()
                .sorted(Comparator.comparing(se -> se.getSportEvent().getEndDate(),
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(LIMIT_OF_TOP_UPCOMING)
                .collect(Collectors.toList());
    }
}
