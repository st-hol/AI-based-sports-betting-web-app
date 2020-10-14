package com.sportbetapp.service.betting.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sportbetapp.domain.betting.SportEvent;
import com.sportbetapp.domain.betting.Wager;
import com.sportbetapp.domain.type.SportType;
import com.sportbetapp.dto.betting.CreateSportEventDto;
import com.sportbetapp.repository.betting.PlayerSideRepository;
import com.sportbetapp.repository.betting.SportEventRepository;
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
    public Optional<SportEvent> findByWager(Wager wager) {
//        return sportEventRepository.findByWager(wager, LocalDateTime.now());
        return null;
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
                .endDate(createSportEventForm.getEndDate())
                .playerSides(Arrays.asList(
                        playerSideRepository.findByNameAndSportType(createSportEventForm.getHomeTeamName(), sportType),
                        playerSideRepository.findByNameAndSportType(createSportEventForm.getAwayTeamName(), sportType)))
                .build();

        //todo make bets

        SportEvent sportEventCreated = sportEventRepository.save(sportEvent);
        log.info("create sport event {}", sportEvent);
        return sportEventCreated;
    }


}
