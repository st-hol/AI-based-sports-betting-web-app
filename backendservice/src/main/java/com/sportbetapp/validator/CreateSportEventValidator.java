package com.sportbetapp.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sportbetapp.domain.betting.SportEvent;
import com.sportbetapp.domain.type.SportType;
import com.sportbetapp.dto.betting.CreateSportEventDto;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("squid:S2068")
@Slf4j
@Component
public class CreateSportEventValidator implements Validator {

    private static final String HOME_TEAM_NAME = "homeTeamName";
    private static final String AWAY_TEAM_NAME = "awayTeamName";
    private static final String END_DATE = "endDate";

    private static final String NOT_EMPTY = "NotEmpty";


    @Override
    public boolean supports(Class<?> aClass) {
        return SportType.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CreateSportEventDto dto = (CreateSportEventDto) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, HOME_TEAM_NAME, NOT_EMPTY);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, AWAY_TEAM_NAME, NOT_EMPTY);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, END_DATE, NOT_EMPTY);

        if (dto.getHomeTeamName().equals(dto.getAwayTeamName())) {
            errors.rejectValue(AWAY_TEAM_NAME, "Can.not.play.against.itself");
        }
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            errors.rejectValue(AWAY_TEAM_NAME, "End.date.must.be.after");
        }

        log.error("{}", errors);
    }

}
