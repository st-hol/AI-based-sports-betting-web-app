package com.sportbetapp.domain.predicting;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.sportbetapp.domain.type.ResultCategory;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
public class HitScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer hitsScored;

    private Integer hitsMissed;

    private ResultCategory resultCategory;

    @OneToOne(mappedBy = "hitScore")
    private PredictionRecord predictionRecord;

}
