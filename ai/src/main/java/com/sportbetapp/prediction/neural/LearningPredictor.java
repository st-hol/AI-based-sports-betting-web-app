/*
 * * Name: Andrew Ward
Student ID: 15002106
 */
package com.sportbetapp.prediction.neural;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;


public class LearningPredictor {

    private static final double NORMALISATION_COEFFICIENT = 0.00392;
    private static final double LEAST_MEAN_SQUARE_ERROR = 0.001;
    private static final double TEACHING_STEP = 0.01;

    //Attributes
    private static final double HOME = 1.0;
    private static final double DRAW = 0.5;
    private static final double AWAY = 0.0;

    //2d array
    private static final double[][] input = {
            //home, away, score, score, outcome
            {1.0, 0.1, 3.0, 2.0, HOME},
            {0.8, 0.4, 1.0, 0.0, HOME},
            {0.5, 0.5, 2.0, 2.0, DRAW},
            {0.3, 0.4, 1.0, 1.9, DRAW},
            {0.5, 0.8, 1.0, 3.0, AWAY}
    };

    //number of inputs in the above array
    private static final int INPUT_PATTERNS = input.length;

    private int maxTests = 5;

    public void processLearn(double homeTeam, double awayTeam) {
        NeuralNet nn = new NeuralNet();
        nn.initializeNeuralNetwork();
        //train NN
        processTraining(nn);
        double average = calculateAverage(homeTeam, awayTeam, nn);
        //Based on result, output either home win/draw/away win
        decideResult(average);
    }

    private void processTraining(NeuralNet nn) {
        double[] weights = nn.getWeights();
        double output;
        double mse = 99;
        int epochs = 0;
        while (fabs(mse - LEAST_MEAN_SQUARE_ERROR) > 0.81) {
            mse = 0;
            double error = 0;

            for (int i = 0; i < maxTests; i++) {
                output = nn.calcWeights(i, input);
                error += fabs(input[i][4] - output);

                weights[0] += TEACHING_STEP * (input[i][4] - output) * input[i][0];
                weights[1] += TEACHING_STEP * (input[i][4] - output) * input[i][1];
                weights[2] += TEACHING_STEP * (input[i][4] - output) * input[i][2];
                weights[3] += TEACHING_STEP * (input[i][4] - output) * input[i][3];
                weights[4] += TEACHING_STEP * (input[i][4] - output);
            }
            mse = error / (double) maxTests;
            epochs++;
//            calcMseForEpoch(nn, mse, epochs);
        }
    }

    private void calcMseForEpoch(NeuralNet nn, double mse, int epochs) {
        double output;
        //run through all input patterns(epochs)
        for (int j = 0; j < INPUT_PATTERNS; j++) {
            //give the two strength values to the network
            for (int k = 0; k < 2; k++) {
                nn.inputAt(k, normalise(input[j][k]));
            }
            //input bias at last position
            nn.inputAt(2, 1);
            output = nn.calculateNet();
        }
        System.out.println("The mean square error of " + epochs +
                " epoch is " + mse);
    }

    private double calculateAverage(double homeTeam, double awayTeam, NeuralNet nn) {
        List<Double> predictionResults = new ArrayList<>();
        double average = 0;
        for (int i = 0; i < maxTests; i++) {
            double result = nn.recall(normalise(homeTeam), normalise(awayTeam), i, input);
            predictionResults.add(result);
            average += result;
            System.out.println("Test " + (i + 1) + ": " + result);
        }

        average = average / maxTests;
        return average;
    }

    private void decideResult(double average) {
        System.out.println("Average: " + average);
        if (average <= 0.3) {
            System.out.println("Win for away team");
            JOptionPane.showMessageDialog(null, "Win for Away Team");
        } else if (average >= 0.5) {
            System.out.println("Win for home team");
            JOptionPane.showMessageDialog(null, "Win for Home team");
        } else {
            System.out.println("Draw");
            JOptionPane.showMessageDialog(null, "draw");
        }
    }


    private static double normalise(double x) {
        return NORMALISATION_COEFFICIENT * x;
    }

    private static double fabs(double n) {
        //if +ve, return value, else return +ve value
        if (n >= 0) {
            return n;
        } else {
            return 0 - n;
        }
    }

}