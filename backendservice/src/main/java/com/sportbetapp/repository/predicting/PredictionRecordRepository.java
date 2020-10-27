package com.sportbetapp.repository.predicting;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sportbetapp.domain.betting.PlayerSide;
import com.sportbetapp.domain.betting.SportEvent;
import com.sportbetapp.domain.predicting.HistoricRecord;
import com.sportbetapp.domain.predicting.HitScore;
import com.sportbetapp.domain.predicting.PredictionRecord;
import com.sportbetapp.domain.predicting.StatisticRecord;


@Repository
public interface PredictionRecordRepository extends CrudRepository<PredictionRecord, Long> {

//    List<PredictionRecord> findAllByPlayerSide(PlayerSide playerSide);


    <T> List<T> findAllByPlayerSide(PlayerSide playerSide);


    default List<PredictionRecord> findAllUsingBothTypes(PlayerSide playerSide) {
        return findAllByPlayerSide(playerSide);
    }

    default List<StatisticRecord> findAllUsingOnlyStatisticType(PlayerSide playerSide) {
        return findAllByPlayerSide(playerSide);
    }

    default List<HistoricRecord> findAllUsingOnlyHistoricType(PlayerSide playerSide) {
        return findAllByPlayerSide(playerSide);
    }


    @Query("select hr from HistoricRecord hr " +
            "where hr.sportEvent = :sportEvent")
    List<PredictionRecord> findHistoricRecordsByEvent(@Param("sportEvent") SportEvent sportEvent);
}
