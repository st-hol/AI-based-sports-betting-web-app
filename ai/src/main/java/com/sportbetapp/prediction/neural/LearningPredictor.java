package com.sportbetapp.prediction.neural;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class LearningPredictor {

    private static final double NORMALISATION_COEFFICIENT = 0.0392;
    private static final double LEAST_MEAN_SQUARE_ERROR = 0.001;
    private static final double TEACHING_STEP = 0.01;
    private static final double EXIT_BOUND = 0.481;

    private static final double HOME = 1.0;
    private static final double DRAW = 0.5;
    private static final double AWAY = 0.0;

    private static final double[][] input = {
            // home, away, score, score, outcome
            {1.0, 0.1, 4.0, 3.0, HOME},
            {0.8, 0.3, 4.0, 2.0, HOME},
            {0.7, 0.4, 4.0, 1.0, HOME},
            {0.6, 0.4, 4.0, 0.0, HOME},
            {0.37, 0.16, 3.5, 2.5, HOME},
            {1.0, 0.1, 3.0, 2.0, HOME},
            {0.8, 0.3, 3.0, 1.0, HOME},
            {0.7, 0.4, 3.0, 0.0, HOME},
            {0.37, 0.16, 2.5, 1.5, HOME},
            {1.0, 0.1, 2.0, 1.0, HOME},
            {0.8, 0.3, 2.0, 0.0, HOME},
            {0.37, 0.16, 1.5, 0.5, HOME},
            {1.0, 0.1, 1.0, 0.0, HOME},

            {0.5, 0.5, 2.0, 2.0, DRAW},
            {0.3, 0.4, 1.0, 1.9, DRAW},

            {0.1, 1.0, 0.0, 1.0, AWAY},
            {0.16, 0.37, 1.5, 0.5, AWAY},
            {0.3, 0.8, 0.0, 2.0, AWAY},
            {0.1, 1.0, 1.0, 2.0, AWAY},
            {0.16, 0.37, 1.5, 2.5, AWAY},
            {0.4, 0.7, 0.0, 3.0, AWAY},
            {0.3, 0.8, 1.0, 3.0, AWAY},
            {0.1, 1.0, 2.0, 3.0, AWAY},
            {0.16, 0.37, 2.5, 3.5, AWAY},
            {0.4, 0.6, 0.0, 4.0, AWAY},
            {0.4, 0.7, 1.0, 4.0, AWAY},
            {0.3, 0.8, 2.0, 4.0, AWAY},
            {0.1, 1.0, 3.0, 4.0, AWAY}
    };
    //number of inputs in the above array
    private static final int MAX_TESTS = input.length;


    public Map<String, String> processLearn(double homeTeam, double awayTeam, int homeScore, int awayScore) {
        NeuralNet nn = new NeuralNet();
        nn.initializeNeuralNetwork();
        //train NN
        processTraining(nn);
        List<Double> recallAllResult = recallAll(homeTeam, awayTeam, homeScore, awayScore, nn);
        double average = recallAllResult.stream().mapToDouble(x -> x).sum() / MAX_TESTS;
        log.info("Average is: {}", average);
        //Based on result, output either home win/draw/away win
        return decideResultBasedOnRecallProportion(recallAllResult);
    }

    private void processTraining(NeuralNet nn) {
        double[] weights = nn.getWeights();
        double output;
        double mse = 99;
        while (fabs(mse - LEAST_MEAN_SQUARE_ERROR) > EXIT_BOUND) {
            double error = 0;

            for (int i = 0; i < MAX_TESTS; i++) {
                output = nn.calcWeights(i, input);
                error += fabs(input[i][4] - output);

                weights[0] += TEACHING_STEP * (input[i][4] - output) * input[i][0];
                weights[1] += TEACHING_STEP * (input[i][4] - output) * input[i][1];
                weights[2] += TEACHING_STEP * (input[i][4] - output) * input[i][2];
                weights[3] += TEACHING_STEP * (input[i][4] - output) * input[i][3];
                weights[4] += TEACHING_STEP * (input[i][4] - output);
            }
            mse = error / (double) MAX_TESTS;
        }
    }

    private List<Double> recallAll(double homeTeam, double awayTeam, int homeScore, int awayScore, NeuralNet nn) {
        List<Double> predictionResults = new ArrayList<>();
        for (int i = 0; i < MAX_TESTS; i++) {
            double result = nn.recall(normalise(homeTeam), normalise(awayTeam),
                    normalise(homeScore), normalise(awayScore), i, input);
            predictionResults.add(result);
            log.info("Test " + i + ": " + result);
        }

        return predictionResults;
    }

    private Map<String, String> decideResultBasedOnRecallProportion(List<Double> recallAllResult) {
        //divide in three parts
        int firstBreak = IntStream.range(0, input.length)
                .filter(i -> DRAW == input[i][4])
                .findFirst()
                .orElse(-1);
        int secondBreak = IntStream.range(0, input.length)
                .filter(i -> AWAY == input[i][4])
                .findFirst()
                .orElse(-1);

        List<Double> homePartition = recallAllResult.subList(0, firstBreak);
        List<Double> drawPartition = recallAllResult.subList(firstBreak, secondBreak);
        List<Double> awayPartition = recallAllResult.subList(secondBreak, MAX_TESTS);

        double homeDiff = homePartition.stream()
                .mapToDouble(Math::abs)
                .sum();
        double drawDiff = drawPartition.stream()
                .mapToDouble(Math::abs)
                .sum(); //Math.abs(drawPartition.size() * 0.5
        // - drawDiff) < 0.05
        double awayDiff = awayPartition.stream()
                .mapToDouble(Math::abs)
                .sum()
                * (1 / homeDiff / drawDiff + 1.07)
                + drawDiff;

        double homeAvg = homePartition.stream()
                .mapToDouble(Math::abs)
                .average().orElse(-1);
        double drawAvg = drawPartition.stream()
                .mapToDouble(Math::abs)
                .average().orElse(-1);
        double awayAvg = awayPartition.stream()
                .mapToDouble(Math::abs)
                .average().orElse(-1);

        if (drawAvg > homeAvg && drawAvg > awayAvg) {
            log.info("Draw");
            return Map.of("home", "draw",
                    "away", "draw");
        }
        if (awayDiff > homeDiff) {
            log.info("Win for away team");
            return Map.of("home", "loss",
                    "away", "win");
        } else {
            log.info("Win for home team");
            return Map.of("home", "win",
                    "away", "loss");
        }
    }

    private static double normalise(double x) {
        return NORMALISATION_COEFFICIENT * x;
    }

    private static double fabs(double n) {
        if (n >= 0) {
            return n;
        } else {
            return 0 - n;
        }
    }


    /**
     * @param average
     * @return result dto
     * @deprecated use decideResultBasedOnRecallProportion()
     */
    @Deprecated
    private Map<String, String> decideResult(double average) {
        log.info("Average: " + average);

        // bonus probability is on hand Home team. So its always little less probable that Home loses
        if (average < 0.5) {
            log.info("Win for away team");
            return Map.of("home", "loss",
                    "away", "win");
        } else if (average >= 0.5) {
            log.info("Win for home team");
            return Map.of("home", "win",
                    "away", "loss");
        } else {
            log.info("Draw");
            return Map.of("home", "draw",
                    "away", "draw");
        }
    }
}
