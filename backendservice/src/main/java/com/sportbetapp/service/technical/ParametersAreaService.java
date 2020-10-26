package com.sportbetapp.service.technical;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sportbetapp.domain.betting.Bet;
import com.sportbetapp.domain.betting.PlayerSide;
import com.sportbetapp.domain.technical.ParametersArea;
import com.sportbetapp.domain.type.SportType;

public interface ParametersAreaService {

    List<ParametersArea> findAll();
    ParametersArea findById(String id);
    ParametersArea save(ParametersArea parametersArea);

}
