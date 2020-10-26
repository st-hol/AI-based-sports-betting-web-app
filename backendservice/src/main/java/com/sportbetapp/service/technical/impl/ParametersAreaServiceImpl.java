package com.sportbetapp.service.technical.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import com.sportbetapp.domain.betting.PlayerSide;
import com.sportbetapp.domain.predicting.HistoricRecord;
import com.sportbetapp.domain.predicting.HitScore;
import com.sportbetapp.domain.predicting.PredictionRecord;
import com.sportbetapp.domain.technical.ParametersArea;
import com.sportbetapp.domain.type.FieldRelation;
import com.sportbetapp.domain.type.ResultCategory;
import com.sportbetapp.domain.type.SportType;
import com.sportbetapp.dto.predicting.PredictionDto;
import com.sportbetapp.exception.CanNotPlayAgainstItselfException;
import com.sportbetapp.exception.NoPredictAnalysisDataAvailableException;
import com.sportbetapp.prediction.classifier.NaiveBayesClassifier;
import com.sportbetapp.prediction.neural.LearningPredictor;
import com.sportbetapp.repository.betting.PlayerSideRepository;
import com.sportbetapp.repository.betting.ResultRepository;
import com.sportbetapp.repository.predicting.HitScoreRepository;
import com.sportbetapp.repository.predicting.PredictionRecordRepository;
import com.sportbetapp.repository.technical.ParametersAreaRepository;
import com.sportbetapp.service.predicting.PredictionService;
import com.sportbetapp.service.technical.ParametersAreaService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ParametersAreaServiceImpl implements ParametersAreaService {


    @Autowired
    private ParametersAreaRepository parametersAreaRepository;

    @Override
    public List<ParametersArea> findAll() {
        return Lists.newArrayList(parametersAreaRepository.findAll());
    }

    @Override
    public ParametersArea findById(String id) {
        return parametersAreaRepository.findById(id).orElse(null);
    }

    @Override
    public ParametersArea save(ParametersArea parametersArea) {
        return parametersAreaRepository.save(parametersArea);
    }

}
