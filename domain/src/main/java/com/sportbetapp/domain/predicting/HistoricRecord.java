package com.sportbetapp.domain.predicting;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.sportbetapp.domain.betting.SportEvent;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@Entity
public class HistoricRecord extends PredictionRecord{

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sport_event_id", referencedColumnName = "id")
    private SportEvent sportEvent;//corresponding sport event
}
