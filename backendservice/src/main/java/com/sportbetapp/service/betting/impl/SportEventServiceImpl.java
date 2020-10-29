package com.sportbetapp.service.betting.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.sportbetapp.domain.betting.SportEvent;
import com.sportbetapp.domain.type.SportType;
import com.sportbetapp.dto.SportEventMessageDto;
import com.sportbetapp.dto.betting.CreateSportEventDto;
import com.sportbetapp.exception.CanNotPlayAgainstItselfException;
import com.sportbetapp.exception.EventAlreadyPredictedException;
import com.sportbetapp.exception.NoPredictAnalysisDataAvailableException;
import com.sportbetapp.jms.JmsProducer;
import com.sportbetapp.repository.betting.PlayerSideRepository;
import com.sportbetapp.repository.betting.SportEventRepository;
import com.sportbetapp.service.betting.BetService;
import com.sportbetapp.service.betting.SportEventService;
import com.sportbetapp.service.predicting.PredictSportEventService;
import com.sportbetapp.service.technical.ParametersAreaService;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class SportEventServiceImpl implements SportEventService {

    @Autowired
    private PredictSportEventService predictSportEventService;

    @Autowired
    private SportEventRepository sportEventRepository;
    @Autowired
    private PlayerSideRepository playerSideRepository;

    @Autowired
    private BetService betService;

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Autowired
    private JmsProducer jmsProducer;

    @Autowired
    private ParametersAreaService parametersAreaService;

    @Override
    public List<SportEvent> findAll() {
        return Lists.newArrayList(sportEventRepository.findAll());
    }

    @Override
    public Page<SportEvent> findAllPageable(Pageable pageable) {
        return sportEventRepository.findAll(pageable);
    }

    @Override
    public SportEvent findById(Long id) {
        return sportEventRepository.findById(id).orElse(null);
    }

    @Override
    public SportEvent save(SportEvent sportEvent) {
        return sportEventRepository.save(sportEvent);
    }

    @Override
    public void deleteAll() {
        sportEventRepository.deleteAll();
    }

    @Override
    public List<SportType> findAllSportTypes() {
        return Arrays.asList(SportType.values());
    }

    @Override
    public SportEvent createNewSportEvent(CreateSportEventDto createSportEventForm) {
        SportType sportType = SportType.valueOf(createSportEventForm.getSportType());
        SportEvent sportEvent = SportEvent.builder().title(createSportEventForm.getTitle())
                .sportType(sportType)
                .startDate(createSportEventForm.getStartDate())
                .playerSides(
                        List.of(playerSideRepository
                                        .findByNameAndSportType(createSportEventForm.getHomeTeamName(), sportType),
                                playerSideRepository
                                        .findByNameAndSportType(createSportEventForm.getAwayTeamName(), sportType)))
                .endDate(createSportEventForm.getEndDate())
                .build();

        SportEvent sportEventCreated = sportEventRepository.save(sportEvent);
        log.info("created new sport event {}", sportEvent);

        scheduleEventPredict(sportEventCreated);
        jmsProducer.sendMessage(SportEventMessageDto.builder()
                .id(sportEventCreated.getId())
                .alreadyPredicted(false)
                .title(sportEventCreated.getTitle())
                .endDate(sportEventCreated.getEndDate())
                .startDate(sportEventCreated.getStartDate())
                .sportType(sportEventCreated.getSportType().toString())
                .build());
        return sportEventCreated;
    } // do not need to create bets. they are not coupled to SE anymore


    private void scheduleEventPredict(SportEvent sportEventCreated) {
        boolean onlyStat = Boolean.parseBoolean(
                parametersAreaService.findById("onlyStat").getValue());

        threadPoolTaskScheduler.schedule(
                new PredictingSportEventScheduledTask(predictSportEventService, sportEventCreated.getId(), onlyStat),
                convertToDateViaInstant(sportEventCreated.getEndDate()));
        // new Date(System.currentTimeMillis() + 5000)
    }

    private Date convertToDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }


    @Getter
    public static class PredictingSportEventScheduledTask implements Runnable {

        private final PredictSportEventService predictSportEventService;

        private final long sportEventId;
        private final boolean onlyStat; //from global param area

        public PredictingSportEventScheduledTask(PredictSportEventService predictSportEventService,
                                                 long sportEventId, boolean onlyStat) {
            this.predictSportEventService = predictSportEventService;
            this.sportEventId = sportEventId;
            this.onlyStat = onlyStat;
        }

        @Override
        public void run() {
            try {
                predictSportEventService.makePredictionForSportEvent(sportEventId, onlyStat);
            } catch (EventAlreadyPredictedException e){
                log.warn("Event was already preicted");
            } catch (CanNotPlayAgainstItselfException | NoPredictAnalysisDataAvailableException e) {
                log.error("An error occurred while processing prediction TASK.");
                throw new RuntimeException(e);
            }
        }
    }
}
