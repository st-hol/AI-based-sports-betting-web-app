package com.sportbetapp.exception;

import com.sportbetapp.dto.predicting.PredictSportEventDto;
import com.sportbetapp.dto.predicting.PredictionDto;

public class NoPredictAnalysisDataAvailableException extends Exception {

    private final PredictSportEventDto dto;

    public NoPredictAnalysisDataAvailableException() {
        dto = stubPredictSportEventDto();
    }

    private PredictSportEventDto stubPredictSportEventDto() {
        PredictSportEventDto predictSportEventDto = new PredictSportEventDto();
        predictSportEventDto.setAwayTeamName("none");
        predictSportEventDto.setHomeTeamName("none");
        return  predictSportEventDto;
    }

    public NoPredictAnalysisDataAvailableException(PredictSportEventDto dto) {
        this.dto = dto;
    }

    public NoPredictAnalysisDataAvailableException(String message, PredictSportEventDto dto) {
        super(message);
        this.dto = dto;
    }

    public NoPredictAnalysisDataAvailableException(String message, Throwable cause, PredictSportEventDto dto) {
        super(message, cause);
        this.dto = dto;
    }

    public NoPredictAnalysisDataAvailableException(Throwable cause, PredictSportEventDto dto) {
        super(cause);
        this.dto = dto;
    }

    public NoPredictAnalysisDataAvailableException(String message, Throwable cause, boolean enableSuppression,
                                                   boolean writableStackTrace, PredictSportEventDto dto) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.dto = dto;
    }

    public PredictSportEventDto getDto() {
        return dto;
    }
}
