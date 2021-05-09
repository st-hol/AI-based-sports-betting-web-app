package com.sportbetapp.prediction.neural;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class LearningPredictor {

    private static final double NORMALISATION_COEFFICIENT = 0.00392;
    private static final double LEAST_MEAN_SQUARE_ERROR = 0.001;
    private static final double TEACHING_STEP = 0.01;

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


    public Map<String, String> processLearn(double homeTeam, double awayTeam) {
        NeuralNet nn = new NeuralNet();
        nn.initializeNeuralNetwork();
        //train NN
        processTraining(nn);
        double average = calculateAverage(homeTeam, awayTeam, nn);
        //Based on result, output either home win/draw/away win
        return decideResult(average);
    }

    private void processTraining(NeuralNet nn) {
        double[] weights = nn.getWeights();
        double output;
        double mse = 99;
        while (fabs(mse - LEAST_MEAN_SQUARE_ERROR) > 0.081) {
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

    private double calculateAverage(double homeTeam, double awayTeam, NeuralNet nn) {
        List<Double> predictionResults = new ArrayList<>();
        double average = 0;
        for (int i = 0; i < MAX_TESTS; i++) {
            double result = nn.recall(normalise(homeTeam), normalise(awayTeam), i, input);
            predictionResults.add(result);
            average += result;
            log.info("Test " + i + ": " + result);
        }

        average = average / MAX_TESTS;
        return average;
    }

    /**
     * @param average
     * @return result dto
     */
    private Map<String, String> decideResult(double average) {
        log.info("Average: " + average);

        // bonus probability is on hand Home team. So its always little less probable that Home loses
        if (average <= 0.39) {
            log.info("Win for away team");
            return Map.of("home", "loss",
                    "away", "win");
        } else if (average >= 0.44) {
            log.info("Win for home team");
            return Map.of("home", "win",
                    "away", "loss");
        } else {
            log.info("Draw");
            return Map.of("home", "draw",
                    "away", "draw");
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

}
