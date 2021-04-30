package com.sportbetapp.prediction.classifier;


import org.apache.commons.lang3.tuple.Pair;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NaiveBayesClassifier {

    private static final double M_COEFFICIENT = 2.0;
    private static final double P_COEFFICIENT = 0.5;

    private static final int NUM_ATTR = 2;
    private static final int TRAIN_SIZE = 15;
    private static final int TEST_SIZE = 1;

    private static final String[] CATEGORIES = new String[]{"win", "draw", "loss"};
    //n of categories (win, draw, lose)
    private static final int N_CATEGORIES = 3;

    //Declare new 2D array of Strings to store teamData.
    private String[][] teamData = new String[6][5];


    /**
     * @param teamData
     * @param testData
     * @param homePred
     * @param awayPred
     * @return maxProb and corresponding Category
     */
    public Pair<Double, String> calculate(String[][] teamData, String[][] testData, int homePred, int awayPred) {
        //sets teamData 2D array
        this.teamData = teamData;

        //Initialises result array with the length for num of categories
        double[] result = new double[N_CATEGORIES];

        double max = -1000.0;
        int maxPosition = -1;
        double average = 0.0;
        //loops for number of test samples (1)
        for (int k = 0; k < TEST_SIZE; k++) {
            //loops for the number of categories (3)
            for (int i = 0; i < N_CATEGORIES; i++) {
                //sets the result as the probability returned for the given category
                result[i] = calculateProbability(testData[k], CATEGORIES[i], homePred, awayPred);
                //prints out probability of each category
                log.info("Probability of category [" + CATEGORIES[i] + "] : " + result[i]);
                if (result[i] > max) {
                    max = result[i];
                    maxPosition = i;
                }
                average += result[i];
            }
            log.info("Naive Bayes Classifier:");
            log.info("the " + k + " test data is classified as category [" + CATEGORIES[maxPosition] + "] with " + max);
        }
        average = average / N_CATEGORIES;
        log.info("Average: " + average);
        final Pair<Double, String> resultPair = Pair.of(max, CATEGORIES[maxPosition]);
        log.info("NBC >>> " + resultPair.toString());
        return resultPair;
    }

    private double calculateProbability(String[] test, String cat, int homePred,
                                        int awayPred) {
        //Declare count array with length num_attr
        int[] count = new int[NUM_ATTR];
        //initialises values in count array to 0
        for (int i = 0; i < NUM_ATTR; i++) {
            count[i] = 0;
        }

        //initialises values to 0.0 and 0
        double pCategory = 0.0;
        int numCategory = 0;

        //loop for the amount of training samples
        for (int j = 0; j < TRAIN_SIZE; j++) {
            //if the category is equal to the category in the final part of the 
            //array, increase the count for that category.
//            System.out.println("j"+ j + "teamData[j]" + teamData[j] + "teamData[j][NUM_ATTR]" + teamData[j][NUM_ATTR]);
            if (cat.equals(teamData[j][NUM_ATTR])) {
                numCategory++;
            }
        }

        pCategory = (double) numCategory / (double) TRAIN_SIZE;

        //loop for the amount of attributes
        for (int i = 0; i < NUM_ATTR; i++) {
            //loop for the amount of training samples
            for (int j = 0; j < TRAIN_SIZE; j++) {
                int num = Integer.parseInt(teamData[j][i]);
                if (j == 0) {
                    if ((num >= homePred) && (cat.equals(teamData[j][NUM_ATTR]))) {
                        count[i]++;
                    }
                } else if (j == 1) {
                    if ((num >= awayPred) && (cat.equals(teamData[j][NUM_ATTR]))) {
                        count[i]++;
                    }
                } else {
                    if ((num >= 1) && (cat.equals(teamData[j][NUM_ATTR]))) {
                        count[i]++;
                    }
                }
            }
            //calculate probability and concatenate to pCategory
            pCategory *= ((double) count[i] + M_COEFFICIENT * P_COEFFICIENT) / ((double) numCategory + M_COEFFICIENT);
            /*Prints out probability of each result given the predicted goals 
            scored and conceded
            E.g. score 2 goals then prob of winning, losing and drawing is output
            based on previous results when scoring multiple goals. 
            */
            log.info("Attribute:Count " + test[i] + " : " + count[i] +
                    " (probability = " + ((double) count[i] + M_COEFFICIENT * P_COEFFICIENT) /
                    ((double) numCategory + M_COEFFICIENT) + ")");

        }
        return pCategory;
    }


}
