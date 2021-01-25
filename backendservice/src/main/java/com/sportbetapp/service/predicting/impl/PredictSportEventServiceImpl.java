package com.sportbetapp.service.predicting.impl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.sportbetapp.domain.betting.PlayerSide;
import com.sportbetapp.domain.betting.SportEvent;
import com.sportbetapp.domain.predicting.HistoricRecord;
import com.sportbetapp.domain.predicting.PredictionRecord;
import com.sportbetapp.domain.type.SportType;
import com.sportbetapp.dto.betting.PlayerSideDto;
import com.sportbetapp.dto.predicting.PredictionDto;
import com.sportbetapp.exception.CanNotPlayAgainstItselfException;
import com.sportbetapp.exception.EventAlreadyPredictedException;
import com.sportbetapp.exception.NoPredictAnalysisDataAvailableException;
import com.sportbetapp.repository.betting.PlayerSideRepository;
import com.sportbetapp.repository.betting.ResultRepository;
import com.sportbetapp.repository.predicting.HitScoreRepository;
import com.sportbetapp.repository.predicting.PredictionRecordRepository;
import com.sportbetapp.service.betting.GameOutcomeDecidingService;
import com.sportbetapp.service.betting.SportEventService;
import com.sportbetapp.service.betting.WagerService;
import com.sportbetapp.service.predicting.PredictSportEventService;

@Service
public class PredictSportEventServiceImpl implements PredictSportEventService {

    @Autowired
    private HitScoreRepository hitScoreRepository;
    @Autowired
    private PredictionRecordRepository predictionRecordRepository;
    @Autowired
    private PlayerSideRepository playerSideRepository;
    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private SportEventService sportEventService;
    @Autowired
    private PredictionServiceImpl predictionService;
    @Autowired
    private WagerService wagerService;
    @Autowired
    private GameOutcomeDecidingService gameOutcomeDecidingService;


    @Override
    public void makePredictionForSportEvent(Long sportEventId, Boolean onlyStat)
            throws CanNotPlayAgainstItselfException, NoPredictAnalysisDataAvailableException, EventAlreadyPredictedException {
        //1. find sport event by Id
        SportEvent sportEvent = sportEventService.findById(sportEventId);

        if (sportEvent.isAlreadyPredicted()){
            throw new EventAlreadyPredictedException();
        }

        List<PlayerSide> playerSides = Lists.newArrayList(sportEvent.getPlayerSides());

        PredictionDto predictionDto = new PredictionDto();
        predictionDto.setSportType(Objects.requireNonNull(playerSides
                .stream()
                .findAny().orElse(null))
                .getSportType().toString());
        predictionDto.setHomeTeamName(playerSides.get(0).getName());
        predictionDto.setAwayTeamName(playerSides.get(1).getName());
        predictionDto.setUseOnlyStatisticRecords(onlyStat);

        //simple prediction
        List<PredictionRecord> historicRecords = predictionService.makePrediction(predictionDto);
        historicRecords.forEach(hr -> ((HistoricRecord) hr).setSportEvent(sportEvent));
        //updating with sport event field
        predictionRecordRepository.saveAll(historicRecords);

        //but also need update result/bet/wagers&outcomes
        gameOutcomeDecidingService.determineResults(sportEvent);
    }


    @Override
    public List<PlayerSideDto> getAllTeamsForSportType(String sportType) {
        if (sportType.isEmpty()) {
            return Collections.emptyList();
        }
        return playerSideRepository.findAllBySportType(SportType.valueOf(sportType))
                .stream().map(ps -> new PlayerSideDto(ps.getName(), ps.getSportType().getValue()))
                .collect(Collectors.toList());
    }



}
