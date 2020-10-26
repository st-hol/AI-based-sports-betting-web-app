package com.sportbetapp.domain.betting;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.format.annotation.DateTimeFormat;

import com.sportbetapp.domain.betting.guess.Guess;
import com.sportbetapp.domain.predicting.HistoricRecord;
import com.sportbetapp.domain.type.Currency;
import com.sportbetapp.domain.type.SportType;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
public class SportEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    private String title;
    private SportType sportType;
    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean alreadyPredicted;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @OneToMany(mappedBy = "sportEvent")
    private List<Guess> guesses;
    @OneToOne(mappedBy = "sportEvent")
    private Result result;
    @OneToMany(mappedBy = "sportEvent")
    private List<HistoricRecord> historicRecords;


    @ManyToMany
    @JoinTable(name = "sport_event_has_player_sides",
            joinColumns = @JoinColumn(name = "sport_event_id"),
            inverseJoinColumns =  {@JoinColumn(name = "player_side_name"), @JoinColumn(name = "player_side_sport_type")})
    private List<PlayerSide> playerSides;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SportEvent that = (SportEvent) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, startDate, endDate, guesses, result);
    }
}
