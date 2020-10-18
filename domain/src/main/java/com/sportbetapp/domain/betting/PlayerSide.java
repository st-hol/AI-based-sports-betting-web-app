package com.sportbetapp.domain.betting;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.sportbetapp.domain.betting.guess.GuessWinner;
import com.sportbetapp.domain.predicting.PredictionRecord;
import com.sportbetapp.domain.type.SportType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(PlayerSide.PlayerSideId.class)
public class PlayerSide {

    @Id
    private String name;

    @Id
    private SportType sportType;

    @OneToMany(mappedBy = "playerSide")
    private List<PredictionRecord> predictionRecords;

    @ManyToOne
    private SportEvent sportEvent;

    @OneToOne(mappedBy = "playerSide")
    private GuessWinner guessWinner;

    @ManyToMany
    @JoinTable(name = "sport_event_has_player_sides",
            joinColumns = {@JoinColumn(name = "player_side_name"), @JoinColumn(name = "player_side_sport_type")},
            inverseJoinColumns = @JoinColumn(name = "sport_event_id"))
    private List<SportEvent> sportEvents;

    @Data
    public static class PlayerSideId implements Serializable {
        private String name;
        private SportType sportType;
    }
}
