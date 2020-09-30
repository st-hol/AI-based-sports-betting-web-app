package com.sportbetapp.domain.betting;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.sportbetapp.domain.type.BetType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Bet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private SportEvent sportEvent;

    private String description;

    private BetType type;

    @OneToMany(mappedBy = "bet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Outcome> outcomes; //different outcomes are created by user

    @Override
    public String toString() {
        return "Bet{" +
                "description='" + description + '\'' +
                ", type=" + type +
                ", outcomes=" + outcomes +
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
        Bet bet = (Bet) o;
        return Objects.equals(sportEvent, bet.sportEvent) &&
                Objects.equals(description, bet.description) &&
                type == bet.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sportEvent, description, type);
    }

}
