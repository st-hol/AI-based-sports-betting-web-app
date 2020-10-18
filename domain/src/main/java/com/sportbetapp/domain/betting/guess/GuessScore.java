package com.sportbetapp.domain.betting.guess;


import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.sportbetapp.domain.betting.PlayerSide;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@Entity
public class GuessScore extends Guess {

    private Integer homeTeamScore;
    private Integer awayTeamScore;

    private String teamNameScored;
    private Integer teamScores;

    private String teamNameMissed;
    private Integer teamMisses;

}
