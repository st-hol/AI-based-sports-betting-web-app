package com.sportbetapp.service.predicting.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
import com.sportbetapp.domain.type.FieldRelation;
import com.sportbetapp.domain.type.ResultCategory;
import com.sportbetapp.domain.type.SportType;
import com.sportbetapp.dto.betting.PlayerSideDto;
import com.sportbetapp.dto.predicting.PredictionDto;
import com.sportbetapp.exception.CanNotPlayAgainstItselfException;
import com.sportbetapp.exception.NoPredictAnalysisDataAvailableException;
import com.sportbetapp.prediction.classifier.NaiveBayesClassifier;
import com.sportbetapp.prediction.neural.LearningPredictor;
import com.sportbetapp.repository.betting.PlayerSideRepository;
import com.sportbetapp.repository.betting.ResultRepository;
import com.sportbetapp.repository.predicting.HitScoreRepository;
import com.sportbetapp.repository.predicting.PredictionRecordRepository;
import com.sportbetapp.service.predicting.PredictionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PredictionServiceImpl implements PredictionService {

    private final Function<PredictionRecord, String[]> predictionTo2dArrayConverter =
            predictionRecord -> new String[]{
                    predictionRecord.getHitScore().getHitsScored().toString(),
                    predictionRecord.getHitScore().getHitsMissed().toString(),
                    predictionRecord.getHitScore().getResultCategory().getValue()
            };

    @Autowired
    private HitScoreRepository hitScoreRepository;
    @Autowired
    private PredictionRecordRepository predictionRecordRepository;
    @Autowired
    private PlayerSideRepository playerSideRepository;
    @Autowired
    private ResultRepository resultRepository;


    @Override
    public List<PredictionRecord> makePrediction(PredictionDto dto)
            throws CanNotPlayAgainstItselfException, NoPredictAnalysisDataAvailableException {
        if (dto.getHomeTeamName().equals(dto.getAwayTeamName())) {
            throw new CanNotPlayAgainstItselfException("can not play against itself", dto);
        }
        return predict(dto);
    }

    private List<PredictionRecord> predict(PredictionDto predictionDto) throws NoPredictAnalysisDataAvailableException {
        int homePred = predictionDto.getGuessHomeTeamScore();   //home team score prediction
        int awayPred = predictionDto.getGuessAwayTeamScore();   //away team score prediction

        //2D arrays for home and away teams, 1 to store previous fixtures,
        //the other prediction. 1 of each for home and away
        predictionDto.setSportType(SportType.FOOTBALL.getValue());//todo stub remove
        String[][] teamData1 = populateTeamDataByPlayerSide(predictionDto, FieldRelation.HOME);
        String[][] testData1 = new String[3][2];
        String[][] teamData2 = populateTeamDataByPlayerSide(predictionDto, FieldRelation.AWAY);
        String[][] testData2 = new String[3][2];

        String[] scoreHome = {String.valueOf(homePred), String.valueOf(awayPred)};
        testData1[0] = scoreHome;
        String[] scoreAway = {String.valueOf(awayPred), String.valueOf(homePred)};
        testData2[0] = scoreAway;

        //when call calculate probability, send team data and score predictions
        //home and away predictions sent in opposite orders due to method taking
        //parameter 2 as goals scored and 1 as goals conceded
        double probTeam1 = new NaiveBayesClassifier().calculate(teamData1, testData1, homePred, awayPred);
        double probTeam2 = new NaiveBayesClassifier().calculate(teamData2, testData2, awayPred, homePred);

        Map<String, String> results = new LearningPredictor().processLearn(probTeam1, probTeam2);
        return savePredictedResults(predictionDto, results, teamData1, teamData2);
    }

    private List<PredictionRecord> savePredictedResults(PredictionDto dto, Map<String, String> result,
                                      String[][] testData1, String[][] testData2) {
        ResultCategory homeTeamResult = ResultCategory.of(result.getOrDefault("home", "draw"));
        ResultCategory awayTeamResult = ResultCategory.of(result.getOrDefault("away", "draw"));

        List<HitScore> hitScores = new ArrayList<>();
        if (homeTeamResult.equals(ResultCategory.WIN) && awayTeamResult.equals(ResultCategory.LOSS)) {
            Pair<Integer, Integer> bestWorstWinScoreHome = populateBestWorstWinScore(testData1);
            Pair<Integer, Integer> bestWorstLossScoreAway = populateBestWorstLossScore(testData2);
            hitScores = populateHitScoreResult(bestWorstWinScoreHome, bestWorstLossScoreAway, FieldRelation.HOME);
        } else if (homeTeamResult.equals(ResultCategory.LOSS) && awayTeamResult.equals(ResultCategory.WIN)) {
            Pair<Integer, Integer> bestWorstLossScoreHome = populateBestWorstLossScore(testData1);
            Pair<Integer, Integer> bestWorstWinScoreAway = populateBestWorstWinScore(testData2);
            hitScores = populateHitScoreResult(bestWorstLossScoreHome, bestWorstWinScoreAway, FieldRelation.AWAY);
        } else if (homeTeamResult.equals(ResultCategory.DRAW) && awayTeamResult.equals(ResultCategory.DRAW)) {
            Pair<Integer, Integer> bestWorstDrawScoreHome = populateBestWorstDrawScore(testData2);
            Pair<Integer, Integer> bestWorstDrawScoreAway = populateBestWorstDrawScore(testData1);
            hitScores = populateHitScoreResult(bestWorstDrawScoreHome, bestWorstDrawScoreAway, FieldRelation.NONE);
        }

        PlayerSide homePlayerSide = playerSideRepository.findByNameAndSportType(dto.getHomeTeamName(),
                SportType.of(dto.getSportType()));
        PlayerSide awayPlayerSide = playerSideRepository.findByNameAndSportType(dto.getAwayTeamName(),
                SportType.of(dto.getSportType()));


        //history save
        List<PlayerSide> playerSides = List.of(homePlayerSide, awayPlayerSide);
        List<PredictionRecord> historicRecords = Streams.zip(hitScores.stream(), playerSides.stream(),
                this::saveToHistoricPredictions)
                .collect(Collectors.toList());

        historicRecords.forEach(rec -> log.info("Historic Record created: {}", rec));

        return historicRecords;
    }

    private Pair<Integer, Integer> populateBestWorstWinScore(String[][] testData) {
        int min = Arrays.stream(testData)
                .filter(testRecord -> ResultCategory.of(testRecord[2]).equals(ResultCategory.WIN))
                .mapToInt(testRecord -> Integer.parseInt(testRecord[0]))
                .min().orElse(0);
        int max = Arrays.stream(testData)
                .filter(testRecord -> ResultCategory.of(testRecord[2]).equals(ResultCategory.WIN))
                .mapToInt(testRecord -> Integer.parseInt(testRecord[0]))
                .max().orElse(0);
        return Pair.of(min, max);
    }

    private Pair<Integer, Integer> populateBestWorstLossScore(String[][] testData) {
        int min = Arrays.stream(testData)
                .filter(testRecord -> ResultCategory.of(testRecord[2]).equals(ResultCategory.LOSS))
                .mapToInt(testRecord -> Integer.parseInt(testRecord[1]))
                .min().orElse(0);
        int max = Arrays.stream(testData)
                .filter(testRecord -> ResultCategory.of(testRecord[2]).equals(ResultCategory.LOSS))
                .mapToInt(testRecord -> Integer.parseInt(testRecord[1]))
                .max().orElse(0);
        return Pair.of(min, max);
    }

    private Pair<Integer, Integer> populateBestWorstDrawScore(String[][] testData) {
        int min = Arrays.stream(testData)
                .filter(testRecord -> ResultCategory.of(testRecord[2]).equals(ResultCategory.DRAW))
                .mapToInt(testRecord -> Integer.parseInt(testRecord[0]))
                .min().orElse(0);
        int max = Arrays.stream(testData)
                .filter(testRecord -> ResultCategory.of(testRecord[2]).equals(ResultCategory.DRAW))
                .mapToInt(testRecord -> Integer.parseInt(testRecord[0]))
                .max().orElse(0);
        return Pair.of(min, max);
    }

    private Pair<Integer, Integer> adjustScoresAccordingToWinner(Pair<Integer, Integer> homeMinMax,
                                                                 Pair<Integer, Integer> awayMinMax,
                                                                 FieldRelation winnerSide) {

        int first = getScoreUsingInts(homeMinMax.getLeft(), homeMinMax.getRight());
        int second = getScoreUsingInts(awayMinMax.getLeft(), awayMinMax.getRight());
        if (winnerSide.equals(FieldRelation.HOME) && first < second) {
            first = first + second;
            second = first - second;
            first = first - second;
        } else if (winnerSide.equals(FieldRelation.AWAY) && first > second) {
            second = second + first;
            first = second - first;
            second = second - first;
        } else if (first == second) {
            int lossScore = first - new Random().nextInt(first);
            if (winnerSide.equals(FieldRelation.HOME)) {
                second = lossScore;
            } else {
                first = lossScore;
            }
        }
        return Pair.of(first, second);
    }

    private List<HitScore> populateHitScoreResult(Pair<Integer, Integer> homeMinMax, Pair<Integer, Integer> awayMinMax,
                                                  FieldRelation winnerSide) {

        Pair<Integer, Integer> homeAwayScore = adjustScoresAccordingToWinner(homeMinMax, awayMinMax, winnerSide);

        int home = homeAwayScore.getLeft();
        int away = homeAwayScore.getRight();

        ResultCategory resultCategory;
        if (winnerSide.equals(FieldRelation.NONE)) {
            resultCategory = ResultCategory.DRAW;
            if (new Random().nextBoolean()) {
                away = home;
            } else {
                home = away;
            }
        } else if (home > away) {
            resultCategory = ResultCategory.WIN;
        } else if (home < away) {
            resultCategory = ResultCategory.LOSS;
        } else {
            throw new UnsupportedOperationException("Can not determine result");
        }
        HitScore hitScoreHome = new HitScore();
        hitScoreHome.setHitsScored(home);
        hitScoreHome.setHitsMissed(away);
        hitScoreHome.setResultCategory(resultCategory);

        HitScore hitScoreAway = new HitScore();
        hitScoreAway.setHitsScored(away);
        hitScoreAway.setHitsMissed(home);
        hitScoreAway.setResultCategory(inverseResult(resultCategory));

        return Lists.newArrayList(hitScoreRepository.saveAll(List.of(hitScoreHome, hitScoreAway)));
    }

    private ResultCategory inverseResult(ResultCategory resultCategory) {
        if (resultCategory.equals(ResultCategory.WIN)) {
            return ResultCategory.LOSS;
        }
        if (resultCategory.equals(ResultCategory.LOSS)) {
            return ResultCategory.WIN;
        } else {
            return ResultCategory.DRAW;
        }
    }

    private int getScoreUsingInts(int min, int max) {
        return new Random().ints(min, max)
                .findFirst()
                .orElse(0);
    }

    private PredictionRecord saveToHistoricPredictions(HitScore hitScore, PlayerSide playerSide) {
        HistoricRecord historicRecord = new HistoricRecord();
        historicRecord.setHitScore(hitScore);
        historicRecord.setPlayerSide(playerSide);

        return predictionRecordRepository.save(historicRecord);
    }

    @NotNull
    private String[][] populateTeamDataByPlayerSide(PredictionDto dto, FieldRelation fieldRelation)
            throws NoPredictAnalysisDataAvailableException {
        String[][] teamData;
        PlayerSide teamPlayer;
        if (fieldRelation.equals(FieldRelation.HOME)) {
            teamPlayer = playerSideRepository.findByNameAndSportType(dto.getHomeTeamName(),
                    SportType.of(dto.getSportType()));
        } else {
            teamPlayer = playerSideRepository.findByNameAndSportType(dto.getAwayTeamName(),
                    SportType.of(dto.getSportType()));
        }

        Supplier<List<? extends PredictionRecord>> teamDataSupplier;
        if (dto.isUseOnlyStatisticRecords()) {
            teamDataSupplier = () -> predictionRecordRepository.findAllUsingOnlyStatisticType(teamPlayer);
        } else {
            teamDataSupplier = () -> predictionRecordRepository.findAllUsingBothTypes(teamPlayer);
        }

        List<? extends PredictionRecord> records =
                Optional.ofNullable(teamDataSupplier.get())
                        .orElseThrow(NoPredictAnalysisDataAvailableException::new);

        teamData = records.stream()
                .map(predictionTo2dArrayConverter)
                .toArray(String[][]::new);
        return teamData;
    }

    @Override
    public List<PlayerSide> getAllTeamsForSportType(SportType sportType) {
        return playerSideRepository.findAllBySportType(sportType);
    }

}
