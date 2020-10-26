package com.sportbetapp.exception;

public class EventAlreadyPredictedException extends Exception {
    public EventAlreadyPredictedException() {
    }

    public EventAlreadyPredictedException(String message) {
        super(message);
    }
}
