package com.sportbetapp.service.scheduling.scheduled;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sportbetapp.service.scheduling.SportEventSchedulingService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PredictionTaskScheduler {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private SportEventSchedulingService sportEventSchedulingService;

    /**
     * every midnight prediction.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void scheduledPrediction() {
        log.info("Scheduled Prediction Started.\n" +
                "The time is now {}", formatter.format(LocalDateTime.now()));
        sportEventSchedulingService.process();
        log.info("Scheduled Prediction Finished.");
    }
}