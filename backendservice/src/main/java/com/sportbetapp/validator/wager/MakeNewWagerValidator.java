package com.sportbetapp.validator.wager;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sportbetapp.domain.betting.SportEvent;
import com.sportbetapp.dto.betting.CreateSportEventDto;
import com.sportbetapp.dto.betting.CreateWagerDto;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("squid:S2068")
@Slf4j
@Component
public class MakeNewWagerValidator implements Validator {

    //messages
    private static final String NOT_EMPTY = "NotEmpty";

    //fields
    private static final String SPORT_TYPE = "sportType";
    private static final String AMOUNT = "amount";
    private static final String CURRENCY = "currency";
    private static final String SPORT_EVENT_ID = "sportEventId";
    private static final String BET_TYPE = "betType";

    @Override
    public boolean supports(Class<?> aClass) {
        return CreateWagerDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CreateWagerDto dto = (CreateWagerDto) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, BET_TYPE, NOT_EMPTY);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, SPORT_EVENT_ID, NOT_EMPTY);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, CURRENCY, NOT_EMPTY);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, AMOUNT, NOT_EMPTY);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, SPORT_TYPE, NOT_EMPTY);

        log.error("{}", errors);
    }

}
