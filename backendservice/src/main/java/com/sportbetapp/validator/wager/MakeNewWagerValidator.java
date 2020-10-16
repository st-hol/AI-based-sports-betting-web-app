package com.sportbetapp.validator.wager;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sportbetapp.domain.betting.SportEvent;
import com.sportbetapp.dto.betting.CreateWagerDto;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("squid:S2068")
@Slf4j
@Component
public class MakeNewWagerValidator implements Validator {

    private static final String SPORT_TYPE = "sportType";
    private static final String NOT_EMPTY = "NotEmpty";

    @Override
    public boolean supports(Class<?> aClass) {
        return CreateWagerDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, SPORT_TYPE, NOT_EMPTY);
        log.error("{}", errors);
    }

}
