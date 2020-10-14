package com.sportbetapp.service.predicting.impl;

import static com.sportbetapp.domain.type.SportType.FOOTBALL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
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
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import com.sportbetapp.domain.betting.Bet;
import com.sportbetapp.domain.betting.PlayerSide;
import com.sportbetapp.domain.betting.SportEvent;
import com.sportbetapp.domain.predicting.HistoricRecord;
import com.sportbetapp.domain.predicting.HitScore;
import com.sportbetapp.domain.predicting.PredictionRecord;
import com.sportbetapp.domain.predicting.StatisticRecord;
import com.sportbetapp.domain.type.FieldRelation;
import com.sportbetapp.domain.type.ResultCategory;
import com.sportbetapp.domain.type.SportType;
import com.sportbetapp.dto.predicting.PredictionDto;
import com.sportbetapp.exception.CanNotPlayAgainstItselfException;
import com.sportbetapp.exception.FileStorageException;
import com.sportbetapp.exception.NoPredictAnalysisDataAvailableException;
import com.sportbetapp.prediction.classifier.NaiveBayesClassifier;
import com.sportbetapp.prediction.neural.LearningPredictor;
import com.sportbetapp.repository.betting.PlayerSideRepository;
import com.sportbetapp.repository.betting.ResultRepository;
import com.sportbetapp.repository.predicting.HitScoreRepository;
import com.sportbetapp.repository.predicting.PredictionRecordRepository;
import com.sportbetapp.service.predicting.PredictionService;
import com.sportbetapp.service.predicting.StatisticAnalyzeService;

@Service
public class StatisticAnalyzeServiceImpl implements StatisticAnalyzeService {


}
