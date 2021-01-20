package com.sportbetapp.service.scheduling;

import java.util.List;

import com.sportbetapp.domain.technical.messaging.UpcomingEventToPredict;

public interface UpcomingEventToPredictService {

    List<UpcomingEventToPredict> findAll();

    UpcomingEventToPredict findById(Long id);

    UpcomingEventToPredict save(UpcomingEventToPredict upcomingEventToPredict);

    void deleteAll();

    List<UpcomingEventToPredict> findTop3Upcoming();
}
