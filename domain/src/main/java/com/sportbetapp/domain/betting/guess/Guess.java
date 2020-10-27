package com.sportbetapp.domain.betting.guess;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.sportbetapp.domain.betting.Bet;
import com.sportbetapp.domain.betting.Result;
import com.sportbetapp.domain.betting.SportEvent;
import com.sportbetapp.domain.betting.Wager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Guess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ManyToOne
    protected Bet bet;

    @ManyToOne
    private SportEvent sportEvent;

    @OneToMany(mappedBy = "guess", orphanRemoval = true)
    protected List<Wager> wagers = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "winner_guesses_has_result",
            joinColumns = @JoinColumn(name = "winner_guess_id"),
            inverseJoinColumns = @JoinColumn(name = "result_id"))
    private Set<Result> results = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Guess guess = (Guess) o;
        return Objects.equals(id, guess.id) &&
                Objects.equals(bet, guess.bet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bet);
    }
}
