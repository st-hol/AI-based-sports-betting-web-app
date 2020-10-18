package com.sportbetapp.fm;

import com.sportbetapp.domain.betting.Wager;
import com.sportbetapp.domain.betting.guess.Guess;
import com.sportbetapp.dto.betting.CreateWagerDto;
import com.sportbetapp.exception.NotExistingGuessException;

public abstract class GuessFactory {

    abstract Guess prepareCertainWagerGuess(final CreateWagerDto createWagerDto) throws NotExistingGuessException;

    public Guess makeWagerGuess(CreateWagerDto createWagerDto) throws NotExistingGuessException {
        return prepareCertainWagerGuess(createWagerDto);
    }

}