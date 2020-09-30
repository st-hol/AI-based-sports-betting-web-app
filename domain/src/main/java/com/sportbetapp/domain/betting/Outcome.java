package com.sportbetapp.domain.betting;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.sportbetapp.domain.user.role.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Outcome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @ManyToOne
    private Bet bet;

    @OneToMany(mappedBy = "outcome", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wager> wagers;

//    @ManyToOne
//    private Result result;

    @ManyToMany
    @JoinTable(name = "winner_outcomes_has_result",
            joinColumns = @JoinColumn(name = "winner_outcome_id"),
            inverseJoinColumns = @JoinColumn(name = "result_id"))
    private Set<Result> results = new HashSet<>();

    @Override
    public String toString() {
        return "Outcome{" +
                "description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Outcome outcome = (Outcome) o;
        return Objects.equals(bet, outcome.bet) &&
                Objects.equals(description, outcome.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bet, description);
    }
}
