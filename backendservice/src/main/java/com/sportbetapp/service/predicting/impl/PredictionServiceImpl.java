package com.sportbetapp.service.predicting.impl;

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
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import com.sportbetapp.domain.betting.PlayerSide;
import com.sportbetapp.domain.betting.SportEvent;
import com.sportbetapp.domain.predicting.HistoricRecord;
import com.sportbetapp.domain.predicting.HitScore;
import com.sportbetapp.domain.predicting.PredictionRecord;
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
import com.sportbetapp.service.predicting.PredictionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PredictionServiceImpl implements PredictionService {

    private static final Map<String, String> AWAY_WIN_MAP = Map.of("away", "win", "home", "loss");
    private static final Map<String, String> HOME_WIN_MAP = Map.of("away", "loss", "home", "win");
    private static final Map<String, String> DRAW_MAP = Map.of("home", "draw", "away", "draw");

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
    @Transactional
    public List<PredictionRecord> makePrediction(PredictionDto dto)
            throws CanNotPlayAgainstItselfException, NoPredictAnalysisDataAvailableException {
        if (dto.getHomeTeamName().equals(dto.getAwayTeamName())) {
            throw new CanNotPlayAgainstItselfException("can not play against itself", dto);
        }
        return predict(dto);
    }

    private List<PredictionRecord> predict(PredictionDto predictionDto) throws NoPredictAnalysisDataAvailableException {
        //2D arrays for home and away teams, 1 to store previous fixtures,
        //the other prediction. 1 of each for home and away
        String[][] teamData1 = populateTeamDataByPlayerSide(predictionDto, FieldRelation.HOME);
        String[][] testData1 = new String[3][2];
        String[][] teamData2 = populateTeamDataByPlayerSide(predictionDto, FieldRelation.AWAY);
        String[][] testData2 = new String[3][2];

        int homePred; //home team score prediction
        int awayPred; //away team score prediction
        if (Objects.isNull(predictionDto.getGuessHomeTeamScore())
                || Objects.isNull(predictionDto.getGuessAwayTeamScore())) {
            homePred = populateBestOrWorstPlayScore(teamData1);
            awayPred = populateBestOrWorstPlayScore(teamData2);
        } else {
            homePred = predictionDto.getGuessHomeTeamScore();
            awayPred = predictionDto.getGuessAwayTeamScore();
        }


        String[] scoreHome = {String.valueOf(homePred), String.valueOf(awayPred)};
        testData1[0] = scoreHome;
        String[] scoreAway = {String.valueOf(awayPred), String.valueOf(homePred)};
        testData2[0] = scoreAway;

        //when call calculate probability, send team data and score predictions
        //home and away predictions sent in opposite orders due to method taking
        //parameter 2 as goals scored and 1 as goals conceded
        Pair<Double, String> probTeam1 = new NaiveBayesClassifier().calculate(teamData1, testData1, homePred, awayPred);
        Pair<Double, String> probTeam2 = new NaiveBayesClassifier().calculate(teamData2, testData2, awayPred, homePred);

        Map<String, String> results;

        if (inControversialCategories(probTeam1, probTeam2)) {
            String winnerSide;
            if (probTeam1.getRight().equals("win")) {
                winnerSide = "home";
            } else {
                winnerSide = "away";
            }
            log.info("Win for {} team", winnerSide);
            results = populateResult(winnerSide);
        } else if (probTeam1.getRight().equals("draw") && probTeam2.getRight().equals("draw")) {
            log.info("DRAW");
            results = populateResult("draw");
        } else {
            results = new LearningPredictor().processLearn(probTeam1.getLeft(), probTeam2.getLeft(), homePred, awayPred);
        }

        return savePredictedResults(predictionDto, results, teamData1, teamData2);
    }

    private Map<String, String> populateResult(String winnerSide) {
        if (winnerSide.equals("away")){
            return AWAY_WIN_MAP;
        }
        if (winnerSide.equals("home")) {
            return HOME_WIN_MAP;
        }
        return DRAW_MAP;
    }

    private boolean inControversialCategories(Pair<Double, String> probTeam1, Pair<Double, String> probTeam2) {
        return probTeam1.getRight().equals("win") && probTeam2.getRight().equals("loss")
                || probTeam1.getRight().equals("loss") && probTeam2.getRight().equals("win");
    }

    private List<PredictionRecord> savePredictedResults(PredictionDto dto, Map<String, String> result,
                                                        String[][] teamData1, String[][] teamData2) {
        ResultCategory homeTeamResult = ResultCategory.of(result.getOrDefault("home", "draw"));
        ResultCategory awayTeamResult = ResultCategory.of(result.getOrDefault("away", "draw"));

        List<HitScore> hitScores = new ArrayList<>();
        if (homeTeamResult.equals(ResultCategory.WIN) && awayTeamResult.equals(ResultCategory.LOSS)) {
            Pair<Integer, Integer> bestWorstWinScoreHome = populateBestWorstWinScore(teamData1);
            Pair<Integer, Integer> bestWorstLossScoreAway = populateBestWorstLossScore(teamData2);
            hitScores = populateHitScoreResult(bestWorstWinScoreHome, bestWorstLossScoreAway, FieldRelation.HOME);
        } else if (homeTeamResult.equals(ResultCategory.LOSS) && awayTeamResult.equals(ResultCategory.WIN)) {
            Pair<Integer, Integer> bestWorstLossScoreHome = populateBestWorstLossScore(teamData1);
            Pair<Integer, Integer> bestWorstWinScoreAway = populateBestWorstWinScore(teamData2);
            hitScores = populateHitScoreResult(bestWorstLossScoreHome, bestWorstWinScoreAway, FieldRelation.AWAY);
        } else if (homeTeamResult.equals(ResultCategory.DRAW) && awayTeamResult.equals(ResultCategory.DRAW)) {
            Pair<Integer, Integer> bestWorstDrawScoreHome = populateBestWorstDrawScore(teamData2);
            Pair<Integer, Integer> bestWorstDrawScoreAway = populateBestWorstDrawScore(teamData1);
            hitScores = populateHitScoreResult(bestWorstDrawScoreHome, bestWorstDrawScoreAway, FieldRelation.NONE);
        }

        PlayerSide homePlayerSide = playerSideRepository.findByNameAndSportType(dto.getHomeTeamName(),
                SportType.valueOf(dto.getSportType()));
        PlayerSide awayPlayerSide = playerSideRepository.findByNameAndSportType(dto.getAwayTeamName(),
                SportType.valueOf(dto.getSportType()));


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

    private int populateBestOrWorstPlayScore(String[][] testData) {
        int min = Arrays.stream(testData)
                .mapToInt(testRecord -> Integer.parseInt(testRecord[0]))
                .min().orElse(0);
        int max = Arrays.stream(testData)
                .mapToInt(testRecord -> Integer.parseInt(testRecord[0]))
                .max().orElse(0);
        return new Random().nextBoolean() ? min : max;
    }

    private Pair<Integer, Integer> adjustScoresAccordingToWinner(Pair<Integer, Integer> homeMinMax,
                                                                 Pair<Integer, Integer> awayMinMax,
                                                                 FieldRelation winnerSide) {

        int first = getScoreUsingInts(homeMinMax.getLeft(), homeMinMax.getRight());
        int second = getScoreUsingInts(awayMinMax.getLeft(), awayMinMax.getRight());
        if (winnerSide.equals(FieldRelation.HOME)) {
            if (first < second) {
                first = first + second;
                second = first - second;
                first = first - second;
            } else if (first == second) {
                second--;
            }
        } else if (winnerSide.equals(FieldRelation.AWAY)) {
            if (first > second) {
                second = second + first;
                first = second - first;
                second = second - first;
            } else if (second == first) {
                first--;
            }
        } else if (winnerSide.equals(FieldRelation.NONE) && first != second) {
            if (new Random().nextBoolean()) {
                second = first;
            } else {
                first = second;
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
        } else if (winnerSide.equals(FieldRelation.HOME)) {
            resultCategory = ResultCategory.WIN;
        } else if (winnerSide.equals(FieldRelation.AWAY)) {
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
                    SportType.valueOf(dto.getSportType()));
        } else {
            teamPlayer = playerSideRepository.findByNameAndSportType(dto.getAwayTeamName(),
                    SportType.valueOf(dto.getSportType()));
        }

        Supplier<List<? extends PredictionRecord>> teamDataSupplier;
        if (dto.getUseOnlyStatisticRecords() != null && dto.getUseOnlyStatisticRecords()) {
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

    @Override
    public List<PredictionRecord> findHistoricRecordsByEvent(SportEvent sportEvent) {
        return predictionRecordRepository.findHistoricRecordsByEvent(sportEvent);
    }

}
