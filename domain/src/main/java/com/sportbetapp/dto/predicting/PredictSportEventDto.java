package com.sportbetapp.dto.predicting;

import lombok.Data;

@Data
public class PredictSportEventDto {
    private String homeTeamName;
    private String awayTeamName;
    private String sportType;
    private boolean useOnlyStatisticRecords;
}
