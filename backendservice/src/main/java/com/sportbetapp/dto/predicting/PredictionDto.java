package com.sportbetapp.dto.predicting;

import lombok.Data;

@Data
public class PredictionDto {
    private String homeTeamName;
    private String awayTeamName;

    private int guessHomeTeamScore;
    private int guessAwayTeamScore;

}
