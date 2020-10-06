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
import org.springframework.security.core.parameters.P;
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
import com.sportbetapp.prediction.payload.PredictedRecordDto;
import com.sportbetapp.repository.betting.PlayerSideRepository;
import com.sportbetapp.repository.betting.ResultRepository;
import com.sportbetapp.repository.predicting.HitScoreRepository;
import com.sportbetapp.repository.predicting.PredictionRecordRepository;
import com.sportbetapp.service.predicting.PredictionService;

@Service
public class PredictionServiceImpl implements PredictionService {

    private static final String ONE_OR_MORE_SPACES = "\\s+";
    private static final char DOT_SIGN = '.';
    private static final String DOUBLE_DOT = "..";
    private static final String TXT_EXT = ".txt";
    private static final String UNDERSCORE = "_";

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
    public void makePredictionForSportEvent(Long sportEventId)
            throws CanNotPlayAgainstItselfException, NoPredictAnalysisDataAvailableException {
        //1. find sport event by Id
        SportEvent sportEvent = new SportEvent() {{
            setBets(Arrays.asList(new Bet() {{
                setStartDate(LocalDateTime.MIN);
                setEndDate(LocalDateTime.MAX);
            }}));
            setPlayerSides(Arrays.asList(
                    new PlayerSide() {{
                        setSportType(FOOTBALL);
                        setName("Bournemouth");
                    }},
                    new PlayerSide() {{
                        setSportType(FOOTBALL);
                        setName("Arsenal");
                    }}
                                        ));
        }};

        List<PlayerSide> playerSides = sportEvent.getPlayerSides();

        PredictionDto predictionDto = new PredictionDto();
        predictionDto.setSportType(Objects.requireNonNull(playerSides
                .stream()
                .findAny().orElse(null))
                .getSportType().getValue());
        predictionDto.setHomeTeamName(playerSides.get(0).getName());
        predictionDto.setAwayTeamName(playerSides.get(1).getName());
        makePrediction(predictionDto);
    }

    @Override
    public void makePrediction(PredictionDto dto)
            throws CanNotPlayAgainstItselfException, NoPredictAnalysisDataAvailableException {
        if (dto.getHomeTeamName().equals(dto.getAwayTeamName())) {
            throw new CanNotPlayAgainstItselfException("can not play against itself", dto);
        }
        predict(dto);
    }

    private void predict(PredictionDto predictionDto) throws NoPredictAnalysisDataAvailableException {
        int homePred = predictionDto.getGuessHomeTeamScore();   //home team score prediction
        int awayPred = predictionDto.getGuessAwayTeamScore();   //away team score prediction

        //2D arrays for home and away teams, 1 to store previous fixtures,
        //the other prediction. 1 of each for home and away
        predictionDto.setSportType(SportType.FOOTBALL.getValue());//todo stub rm
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
        saveResults(predictionDto, results, teamData1, teamData2);
    }

    private void saveResults(PredictionDto dto, Map<String, String> result,
                             String[][] testData1, String[][] testData2) {
        ResultCategory homeTeamResult = ResultCategory.of(result.getOrDefault("home", "draw"));
        ResultCategory awayTeamResult = ResultCategory.of(result.getOrDefault("away", "draw"));

        List<HitScore> hitScores = new ArrayList<>();
        if (homeTeamResult.equals(ResultCategory.WIN) && awayTeamResult.equals(ResultCategory.LOSS)) {
            Pair<Integer, Integer> bestWorstWinScoreHome = populateBestWorstWinScore(testData1);
            Pair<Integer, Integer> bestWorstLossScoreAway = populateBestWorstLossScore(testData2);
            hitScores = populateHitScoreResult(bestWorstWinScoreHome, bestWorstLossScoreAway, false);
        } else if (homeTeamResult.equals(ResultCategory.LOSS) && awayTeamResult.equals(ResultCategory.WIN)) {
            Pair<Integer, Integer> bestWorstLossScoreHome = populateBestWorstLossScore(testData1);
            Pair<Integer, Integer> bestWorstWinScoreAway = populateBestWorstWinScore(testData2);
            hitScores = populateHitScoreResult(bestWorstLossScoreHome, bestWorstWinScoreAway, false);
        } else if (homeTeamResult.equals(ResultCategory.DRAW) && awayTeamResult.equals(ResultCategory.DRAW)) {
            Pair<Integer, Integer> bestWorstDrawScoreHome = populateBestWorstDrawScore(testData2);
            Pair<Integer, Integer> bestWorstDrawScoreAway = populateBestWorstDrawScore(testData1);
            hitScores = populateHitScoreResult(bestWorstDrawScoreHome, bestWorstDrawScoreAway, true);
        }

        PlayerSide homePlayerSide = playerSideRepository.findByNameAndSportType(dto.getHomeTeamName(),
                SportType.of(dto.getSportType()));
        PlayerSide awayPlayerSide = playerSideRepository.findByNameAndSportType(dto.getAwayTeamName(),
                SportType.of(dto.getSportType()));

        //history save
        Streams.zip(hitScores.stream(), List.of(homePlayerSide, awayPlayerSide).stream(),
                this::saveToHistoricPredictions);

        //res

        //wager
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

    private List<HitScore> populateHitScoreResult(Pair<Integer, Integer> homeMinMax, Pair<Integer, Integer> awayMinMax,
                                                  boolean isDraw) {
        int first = getScoreUsingInts(homeMinMax.getLeft(), homeMinMax.getRight());
        int second = getScoreUsingInts(awayMinMax.getLeft(), awayMinMax.getRight());
        ResultCategory resultCategory;
        if (isDraw) {
            resultCategory = ResultCategory.DRAW;
            if (new Random().nextBoolean()) {
                second = first;
            } else {
                first = second;
            }
        } else if (first > second) {
            resultCategory = ResultCategory.WIN;
        } else if (first < second) {
            resultCategory = ResultCategory.LOSS;
        } else {
            throw new UnsupportedOperationException("Can not determine result");
        }
        HitScore hitScoreHome = new HitScore();
        hitScoreHome.setHitsScored(first);
        hitScoreHome.setHitsMissed(second);
        hitScoreHome.setResultCategory(resultCategory);

        HitScore hitScoreAway = new HitScore();
        hitScoreAway.setHitsScored(second);
        hitScoreAway.setHitsMissed(first);
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
