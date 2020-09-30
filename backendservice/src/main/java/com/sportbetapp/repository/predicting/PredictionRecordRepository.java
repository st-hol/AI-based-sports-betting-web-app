package com.sportbetapp.repository.predicting;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sportbetapp.domain.predicting.HitScore;
import com.sportbetapp.domain.predicting.PredictionRecord;


@Repository
public interface PredictionRecordRepository extends CrudRepository<PredictionRecord, Long> {
}
