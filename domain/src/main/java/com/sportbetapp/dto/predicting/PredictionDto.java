package com.sportbetapp.dto.predicting;

import lombok.Data;

@Data
public class PredictionDto {
    private String homeTeamName;
    private String awayTeamName;

    private Integer guessHomeTeamScore;
    private Integer guessAwayTeamScore;

    private String sportType;
    private Boolean useOnlyStatisticRecords;
}
