package com.sportbetapp.domain.betting.guess;


import javax.persistence.Entity;

import com.sportbetapp.domain.type.GoalsDirection;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@Entity
public class GuessGoals extends Guess {

    private String teamName;
    private Integer goalsCount;
    private GoalsDirection goalsDirection;
}
