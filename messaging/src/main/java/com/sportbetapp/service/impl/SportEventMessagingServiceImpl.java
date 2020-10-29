package com.sportbetapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sportbetapp.domain.betting.SportEvent;
import com.sportbetapp.domain.technical.messaging.UpcomingEventToPredict;
import com.sportbetapp.dto.SportEventMessageDto;
import com.sportbetapp.repository.betting.SportEventRepository;
import com.sportbetapp.repository.technical.messaging.UpcomingEventToPredictRepository;
import com.sportbetapp.service.SportEventMessagingService;

@Service
public class SportEventMessagingServiceImpl implements SportEventMessagingService {

    @Autowired
    private UpcomingEventToPredictRepository upcomingEventToPredictRepository;
    @Autowired
    private SportEventRepository sportEventRepository;


    @Override
    public void onMessageReceivedPopulateUpcoming(SportEventMessageDto sportEventMessageDto) {
        SportEvent sportEvent = sportEventRepository.findById(sportEventMessageDto.getId())
                .orElseThrow(IllegalStateException::new);
        UpcomingEventToPredict upcomingEventToPredict = new UpcomingEventToPredict();
        upcomingEventToPredict.setSportEvent(sportEvent);
        upcomingEventToPredictRepository.save(upcomingEventToPredict);
    }
}
