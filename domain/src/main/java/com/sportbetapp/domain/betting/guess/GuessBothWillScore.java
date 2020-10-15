package com.sportbetapp.domain.betting.guess;


import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@Entity
public class GuessBothWillScore extends Guess {

    private Boolean bothScoredAtLeastOne;
    private Boolean bothNotScoredAnyGoals;

}
