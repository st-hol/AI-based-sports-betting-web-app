package com.sportbetapp.service.scheduling.impl;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.sportbetapp.concurrent.BatchExecutionService;
import com.sportbetapp.domain.technical.messaging.UpcomingEventToPredict;
import com.sportbetapp.repository.betting.SportEventRepository;
import com.sportbetapp.repository.technical.messaging.UpcomingEventToPredictRepository;
import com.sportbetapp.service.betting.impl.SportEventServiceImpl;
import com.sportbetapp.service.predicting.PredictSportEventService;
import com.sportbetapp.service.scheduling.SportEventSchedulingService;
import com.sportbetapp.service.technical.ParametersAreaService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SportEventSchedulingServiceImpl implements SportEventSchedulingService {

    @Value("${scheduler.predict.num.threads:0}")
    private int numThreads;

    @Autowired
    private ParametersAreaService parametersAreaService;
    @Autowired
    private UpcomingEventToPredictRepository upcomingEventToPredictRepository;
    @Autowired
    private SportEventRepository sportEventRepository;
    @Autowired
    private PredictSportEventService predictSportEventService;

    @Override
    public void process() {
        List<? extends Runnable> tasks = buildUpcomingEventsProcessingTasks();
        BatchExecutionService.execute(tasks, getNumCompilationThreads(), "daily-scheduler",
                new ClearUpcomingEventsAfterPredictionTask());
    }

    private List<? extends Runnable> buildUpcomingEventsProcessingTasks() {
        final List<Runnable> tasks = new LinkedList<>();
        Runnable currentTask;
        //1. Get all records from upcoming repo
        List<UpcomingEventToPredict> events = Lists.newArrayList(upcomingEventToPredictRepository.findAll());
        //2.
        boolean onlyStat = Boolean.parseBoolean(
                parametersAreaService.findById("onlyStat").getValue());
        for (UpcomingEventToPredict event : events) {
            currentTask = new SportEventServiceImpl.PredictingSportEventScheduledTask(predictSportEventService,
                    event.getSportEvent().getId(),  onlyStat);
            tasks.add(currentTask);
        }

        tasks.sort(Comparator.comparing(
                task -> ((SportEventServiceImpl.PredictingSportEventScheduledTask) task).getSportEventId()));

        return tasks;
    }

    private int getNumCompilationThreads() {
        if (numThreads < 1) {
            return Runtime.getRuntime().availableProcessors() + 1;
        } else {
            return numThreads;
        }
    }


    private class ClearUpcomingEventsAfterPredictionTask implements Runnable {

        @Override
        public void run() {
            upcomingEventToPredictRepository.deleteAll();
        }
    }

}
