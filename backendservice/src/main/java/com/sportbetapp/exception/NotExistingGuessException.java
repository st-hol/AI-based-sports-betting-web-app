package com.sportbetapp.exception;

import com.sportbetapp.dto.betting.CreateWagerDto;

public class NotExistingGuessException extends Exception {

    private CreateWagerDto wagerDto;

    public NotExistingGuessException() {
    }

    public NotExistingGuessException(String message) {
        super(message);
    }

    public NotExistingGuessException(String message, CreateWagerDto wagerDto) {
        super(message);
        this.wagerDto = wagerDto;
    }

    public CreateWagerDto getWagerDto() {
        return wagerDto;
    }
}
