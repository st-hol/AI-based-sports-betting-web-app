package com.sportbetapp.dto.betting;

import java.math.BigDecimal;

import com.sportbetapp.domain.type.BetType;
import com.sportbetapp.domain.type.Currency;

import lombok.Data;

@Data
public class CreateWagerDto {
    private BetType betType;
    private Long sportEventId;

    //wager
    private Currency currency;
    private BigDecimal amount;

    private String sportType;

    //guess
    private String winnerPlayerSideName;

    private Integer homeTeamScore;
    private Integer awayTeamScore;

    private Boolean bothScoredAtLeastOne;

    private Boolean bothNotScoredAnyGoals;

    private String teamNameThatScoredCertainNumOfGoals;
    private Integer numGoalsScoredByCertainTeam;

    private String teamNameThatMissedCertainNumOfGoals;
    private Integer numGoalsMissedByCertainTeam;

    private String teamNameThatScoredGoalsMoreThanCertainNum;
    private Integer goalsScoredMoreThanCertainNumByTeam;

    private String teamNameThatMissedGoalsMoreThanCertainNum;
    private Integer goalsMissedMoreThanCertainNumByTeam;

}
