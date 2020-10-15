package com.sportbetapp.domain.betting;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;


import com.sportbetapp.domain.betting.guess.Guess;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sport_event_id", referencedColumnName = "id")
    private SportEvent sportEvent;

    @ManyToMany
    @JoinTable(name = "winner_guesses_has_result",
            joinColumns = @JoinColumn(name = "result_id"),
            inverseJoinColumns = @JoinColumn(name = "winner_guess_id"))
    private Set<Guess> winnerGuesses; // only one result for all outcomes so it is winner

}
