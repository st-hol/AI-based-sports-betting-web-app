package com.sportbetapp.exception;

import com.sportbetapp.dto.predicting.PredictionDto;

public class CanNotPlayAgainstItselfException extends Exception {

    private final PredictionDto dto;

    public CanNotPlayAgainstItselfException(PredictionDto dto) {
        this.dto = dto;
    }

    public CanNotPlayAgainstItselfException(String message, PredictionDto dto) {
        super(message);
        this.dto = dto;
    }

    public CanNotPlayAgainstItselfException(String message, Throwable cause, PredictionDto dto) {
        super(message, cause);
        this.dto = dto;
    }

    public CanNotPlayAgainstItselfException(Throwable cause, PredictionDto dto) {
        super(cause);
        this.dto = dto;
    }

    public CanNotPlayAgainstItselfException(String message, Throwable cause, boolean enableSuppression,
                                            boolean writableStackTrace, PredictionDto dto) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.dto = dto;
    }

    public PredictionDto getDto() {
        return dto;
    }
}
