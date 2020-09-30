package com.sportbetapp.service.predicting;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sportbetapp.domain.betting.PlayerSide;
import com.sportbetapp.domain.type.SportType;
import com.sportbetapp.dto.predicting.PredictionDto;
import com.sportbetapp.exception.CanNotPlayAgainstItselfException;

public interface PredictionService {

    void makePrediction(PredictionDto dto) throws CanNotPlayAgainstItselfException;

    void processStatisticFile(MultipartFile file);

    List<PlayerSide> getAllTeamsForSportType(SportType sportType);
}
