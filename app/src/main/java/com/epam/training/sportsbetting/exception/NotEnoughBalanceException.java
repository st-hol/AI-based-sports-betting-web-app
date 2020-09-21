package com.epam.training.sportsbetting.exception;

import com.epam.training.sportsbetting.dto.CreateWagerDto;

public class NotEnoughBalanceException extends Exception {

    private CreateWagerDto wagerDto;

    public NotEnoughBalanceException() {
    }

    public NotEnoughBalanceException(String message) {
        super(message);
    }

    public NotEnoughBalanceException(String message, CreateWagerDto wagerDto) {
        super(message);
        this.wagerDto = wagerDto;
    }

    public CreateWagerDto getWagerDto() {
        return wagerDto;
    }
}
