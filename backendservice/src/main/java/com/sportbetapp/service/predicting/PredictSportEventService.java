package com.sportbetapp.service.predicting;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sportbetapp.domain.betting.PlayerSide;
import com.sportbetapp.domain.type.SportType;
import com.sportbetapp.dto.betting.PlayerSideDto;
import com.sportbetapp.dto.predicting.PredictionDto;
import com.sportbetapp.exception.CanNotPlayAgainstItselfException;
import com.sportbetapp.exception.NoPredictAnalysisDataAvailableException;

public interface PredictSportEventService {

    void makePredictionForSportEvent(Long sportEventId) throws CanNotPlayAgainstItselfException,
            NoPredictAnalysisDataAvailableException;

    List<PlayerSideDto> getAllTeamsForSportType(String sportType);
}
