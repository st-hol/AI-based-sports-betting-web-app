package com.epam.training.sportsbetting.exception;

public class EventAlreadyStartedException extends Exception {
    public EventAlreadyStartedException() {
    }

    public EventAlreadyStartedException(String message) {
        super(message);
    }
}
