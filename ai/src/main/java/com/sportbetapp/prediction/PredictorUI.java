package com.sportbetapp.prediction;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

import com.sportbetapp.prediction.classifier.NaiveBayesClassifier;
import com.sportbetapp.prediction.neural.LearningPredictor;

/**
 * @deprecated : it was just for test purpose
 */
@Deprecated
public class PredictorUI {

    //Array of strings for team names
    private final String[] data = {"Arsenal", "Bournemouth", "Brighton", "Burnley",
            "Chelsea", "Crystal Palace", "Everton", "Huddersfield", "Leicester",
            "Liverpool", "Manchester City", "Manchester United", "Newcastle",
            "Southampton", "Watford", "West Ham"};
    private int homePred;   //home team score prediction
    private int awayPred;   //away team score prediction
    private String homeTeamName;    //home team name
    private String awayTeamName;    //away team name
    private int numFixt = 38;       //number of fixtures
    private int numFields = 3;      //number of fields for each fixture

    //2D arrays for home and away teams, 1 to store previous fixtures,
    //the other prediction. 1 of each for home and away
    private String[][] teamData1 = new String[numFixt][numFields];
    private String[][] testData1 = new String[3][2];
    private String[][] teamData2 = new String[numFixt][numFields];
    private String[][] testData2 = new String[3][2];


    //constructor
    public PredictorUI() {
        //builds GUI
        buildUI();
    }

    //methods

    /**
     * Builds GUI and calls methods on action listeners
     */
    public void buildUI() {
        JFrame f = new JFrame("");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(250, 250);
        f.setLocation(300, 200);

        JPanel p1 = new JPanel();

        JLabel l1 = new JLabel("Home Team");
        JLabel l2 = new JLabel("Away Team");
        GridLayout g = new GridLayout(0, 2);
        JComboBox teamHomeCombo = new JComboBox(this.data);
        teamHomeCombo.setSize(50, 20);
        teamHomeCombo.setSelectedIndex(0);
        p1.add(l1);
        p1.add(teamHomeCombo, BorderLayout.NORTH);

        JComboBox teamAwayCombo = new JComboBox(this.data);
        teamAwayCombo.setSize(50, 20);
        teamAwayCombo.setSelectedIndex(0);
        p1.add(l2);
        p1.add(teamAwayCombo, BorderLayout.SOUTH);

        f.add(p1);
        Container teams = new Container();
        teams.setLayout(g);
        JTextField homeTeam = new JTextField(10);
        JLabel lbl1 = new JLabel("Home Team");
        JTextField awayTeam = new JTextField(10);
        JLabel lbl2 = new JLabel("Away Team");
        teams.add(lbl1);
        teams.add(homeTeam);
        teams.add(lbl2);
        teams.add(awayTeam);
        JButton submit = new JButton("Submit");

        submit.addActionListener(e -> {
            //gets selected teams
            homeTeamName = (String) teamHomeCombo.getSelectedItem();
            awayTeamName = (String) teamAwayCombo.getSelectedItem();
            //Parses string in text box to a number
            homePred = Integer.parseInt(homeTeam.getText());
            awayPred = Integer.parseInt(awayTeam.getText());

            String[] scoreHome = {homeTeam.getText(), awayTeam.getText()};
            testData1[0] = scoreHome;
            String[] scoreAway = {awayTeam.getText(), homeTeam.getText()};
            testData2[0] = scoreAway;
            homeTeam.setText("");
            awayTeam.setText("");

            try {
                //Loads relevant team results from text file
                loadTeams(homeTeamName, 'h');
                loadTeams(awayTeamName, 'a');
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PredictorUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            predict();
        });
        teams.add(submit);
        f.add(teams, BorderLayout.SOUTH);
        f.setVisible(true);
    }


    public void predict() {
        //create new Naive Bayes classifiers for home and away
        NaiveBayesClassifier team1 = new NaiveBayesClassifier();
        NaiveBayesClassifier team2 = new NaiveBayesClassifier();

        //when call calculate probability, send team data and score predictions
        //home and away predictions sent in opposite orders due to method taking 
        //parameter 2 as goals scored and 4 as goals conceded
        double probTeam1 = team1.calculate(teamData1, testData1, homePred, awayPred);
        double probTeam2 = team2.calculate(teamData2, testData2, awayPred, homePred);

        //Create new neural net
        new LearningPredictor().processLearn(probTeam1, probTeam2);
    }

    /**
     * Reads relevant files and populates arrays
     *
     * @param teamName name
     * @param homeOrAway (h or a)
     * @throws FileNotFoundException
     */
    private void loadTeams(String teamName, char homeOrAway) throws FileNotFoundException {
        //file path for results files
//        String filePath = "src\\footballresultspredictionnbnn\\Results\\"+teamName+".txt";
        String filePath = "D:\\TEMP\\study\\diplome\\project\\AI-based-sports-betting-web-app\\ai\\src\\main" +
                "\\resources\\example_data\\" + teamName + ".txt";
        //todo
        //create new file
        File file = new File("");
        //Create new file with the filepath of the project+filePath
        //Should allow files to still be read when moved to different PC.
        File f = new File(file + filePath);
        //System.out.println(file.getAbsolutePath());
        Scanner team = new Scanner(f);

        String token = "";
        //String[] homeRes = new String[5];
        int j = 0;
        System.out.println(teamName);
        //Reads file and inserts contents into 2d arrays
        while (team.hasNext()) {
            token = team.nextLine();
            //System.out.println(token);
            //splits token and inserts each part into part of an array
            String[] result = token.split("\t");
            //inserts array to relevant 2d array
            if (homeOrAway == 'h') {
                teamData1[j] = result;
            } else if (homeOrAway == 'a') {
                teamData2[j] = result;
            }
            j++;
        }
        //closes file
        team.close();
    }
}
