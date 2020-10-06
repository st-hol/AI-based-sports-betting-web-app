package com.sportbetapp.prediction.neural;

import java.util.Random;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NeuralNet {

    private static final double BOUND = 10.0;
    //declare number of inputs
    private static final int NUM_INPUTS = 4;
    //create input layer with 1+numInputs to make room for bias
    private double[] inputLayer = new double[NUM_INPUTS + 1];
    //create array of weights, amount=numInputs plus 1 for bias
    private double[] weights = new double[NUM_INPUTS + 1];

    private Random rand = new Random();

    public void initializeNeuralNetwork() {
        //loop to populate weights with values from -0.5 to 0.5
        for (int i = 0; i < NUM_INPUTS + 1; i++) {
            weights[i] = populateWeightInRange(rand, 0.5);
        }
        //activationFunction
    }

    private double populateWeightInRange(Random rand, double range) {
        return ((double) rand.nextInt((int) BOUND) / (BOUND + 1.0)) - range;
    }

    public double calculateNet() {
        double action = 0.0;
        //get action potential for this input pattern
        for (int i = 0; i < NUM_INPUTS; i++) {
            action += inputLayer[i] * weights[i];
        }

        //sigmoid function
        action = 1.0 / (1.0 + Math.exp(-action));

        return action;
    }

    public double recall(double d, double e, int i, double[][] input) {
        //num inputs
        inputLayer[0] = d;
        inputLayer[1] = e;
        inputLayer[2] = 1.0;

        return calcWeights(i, input);
    }

    public double calcWeights(int test, double[][] input) {
        double result;

        result = (input[test][0] * weights[0]) +
                 (input[test][1] * weights[1]) +
                 (input[test][2] * weights[2]) +
                 (input[test][3] * weights[3]) +
                 (1.0 * weights[4]); // bias

        result = 1.0 / (1.0 + Math.exp(-result));

        return result;

    }

    public void inputAt(int inputPosition, double d) {
        inputLayer[inputPosition] = d;
    }

    public double[] getWeights() {
        return weights;
    }
}
