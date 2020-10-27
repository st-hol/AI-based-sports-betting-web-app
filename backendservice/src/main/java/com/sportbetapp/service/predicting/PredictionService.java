package com.sportbetapp.service.predicting;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sportbetapp.domain.betting.PlayerSide;
import com.sportbetapp.domain.betting.SportEvent;
import com.sportbetapp.domain.predicting.PredictionRecord;
import com.sportbetapp.domain.type.SportType;
import com.sportbetapp.dto.predicting.PredictionDto;
import com.sportbetapp.exception.CanNotPlayAgainstItselfException;
import com.sportbetapp.exception.NoPredictAnalysisDataAvailableException;

public interface PredictionService {

    List<PredictionRecord> makePrediction(PredictionDto dto) throws CanNotPlayAgainstItselfException,
            NoPredictAnalysisDataAvailableException;

    List<PlayerSide> getAllTeamsForSportType(SportType sportType);

    List<PredictionRecord> findHistoricRecordsByEvent(SportEvent sportEvent);
}
