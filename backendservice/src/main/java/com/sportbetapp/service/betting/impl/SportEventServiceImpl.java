package com.sportbetapp.service.betting.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;
import com.sportbetapp.domain.betting.Bet;
import com.sportbetapp.domain.betting.SportEvent;
import com.sportbetapp.domain.betting.Wager;
import com.sportbetapp.domain.type.SportType;
import com.sportbetapp.dto.betting.CreateSportEventDto;
import com.sportbetapp.repository.betting.PlayerSideRepository;
import com.sportbetapp.repository.betting.SportEventRepository;
import com.sportbetapp.service.betting.BetService;
import com.sportbetapp.service.betting.SportEventService;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class SportEventServiceImpl implements SportEventService {

    @Autowired
    private SportEventRepository sportEventRepository;
    @Autowired
    private PlayerSideRepository playerSideRepository;

    @Autowired
    private BetService betService;

    @Override
    public List<SportEvent> findAll() {
        return Lists.newArrayList(sportEventRepository.findAll());
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
        return sportEventCreated;
    } // do not need to create bets. they are not coupled to SE anymore


}
