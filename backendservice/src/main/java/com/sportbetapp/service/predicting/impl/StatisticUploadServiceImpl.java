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
import com.sportbetapp.service.predicting.StatisticAnalyzeService;
import com.sportbetapp.service.predicting.StatisticUploadService;

@Service
public class StatisticUploadServiceImpl implements StatisticUploadService {

    private static final String ONE_OR_MORE_SPACES = "\\s+";
    private static final char DOT_SIGN = '.';
    private static final String DOUBLE_DOT = "..";
    private static final String TXT_EXT = ".txt";
    private static final String UNDERSCORE = "_";

    @Autowired
    private HitScoreRepository hitScoreRepository;
    @Autowired
    private PredictionRecordRepository predictionRecordRepository;
    @Autowired
    private PlayerSideRepository playerSideRepository;


    @Override
    public List<PlayerSide> getAllTeamsForSportType(SportType sportType) {
        return playerSideRepository.findAllBySportType(sportType);
    }


    @Override
    public void processStatisticFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains(DOUBLE_DOT)) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            if (!fileName.substring(fileName.lastIndexOf(DOT_SIGN)).equals(TXT_EXT)) {
                throw new FileStorageException("Sorry! Invalid File format " + fileName);
            }

            populatePredictionStatisticsFromFile(file);

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }


    private void populatePredictionStatisticsFromFile(MultipartFile multipartFile) throws IOException {
        Stream<String> linesStream = new BufferedReader(
                new InputStreamReader(multipartFile.getInputStream(), StandardCharsets.UTF_8)).lines();
        PlayerSide playerSide = storePlayerSide(multipartFile);

        List<HitScore> hitScores = linesStream
                .map(this::extractRecordLine)
                .collect(Collectors.toList());
        hitScoreRepository.saveAll(hitScores);

        List<PredictionRecord> predictionRecords = hitScores.stream()
                .map(hitScore -> populatePredictionRecordFromScoreAndPlayerSide(hitScore, playerSide))
                .collect(Collectors.toList());
        predictionRecordRepository.saveAll(predictionRecords);

    }

    private PlayerSide storePlayerSide(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        assert fileName != null;
        String sportTypeAndTeamName = fileName.substring(0, fileName.lastIndexOf(DOT_SIGN));
        Pair<SportType, String> sportTypeTeamNamePair = Pair.of(
                SportType.of(sportTypeAndTeamName.split(UNDERSCORE)[0]),
                sportTypeAndTeamName.split(UNDERSCORE)[1]);
        if (playerSideRepository.existsByName(sportTypeAndTeamName)) {
            return playerSideRepository.findByNameAndSportType(sportTypeTeamNamePair.getRight(),
                    sportTypeTeamNamePair.getLeft());
        } else {
            return playerSideRepository.save(
                    PlayerSide.builder()
                            .name(sportTypeTeamNamePair.getRight())
                            .sportType(sportTypeTeamNamePair.getLeft())
                            .build());
        }
    }

    private HitScore extractRecordLine(String line) {
        String[] values = line.split(ONE_OR_MORE_SPACES);

        HitScore hitScore = new HitScore();
        hitScore.setHitsScored(Integer.valueOf(values[0]));
        hitScore.setHitsMissed(Integer.valueOf(values[1]));
        hitScore.setResultCategory(ResultCategory.of(values[2]));
        return hitScore;
    }

    private PredictionRecord populatePredictionRecordFromScoreAndPlayerSide(HitScore hitScore,
                                                                            PlayerSide playerSide) {
        StatisticRecord statisticRecord = new StatisticRecord();
        statisticRecord.setHitScore(hitScore);
        statisticRecord.setPlayerSide(playerSide);
        return statisticRecord;
    }
}
