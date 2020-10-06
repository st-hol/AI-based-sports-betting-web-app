package com.sportbetapp.domain.betting;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.sportbetapp.domain.predicting.PredictionRecord;
import com.sportbetapp.domain.type.SportType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
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

    @OneToMany(mappedBy = "playerSide", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PredictionRecord> predictionRecords;

    @ManyToOne
    private SportEvent sportEvent;

    @Data
    static class PlayerSideId implements Serializable {
        private String name;
        private SportType sportType;
    }
}
