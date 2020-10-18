package com.sportbetapp.domain.type;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.sportbetapp.domain.type.typed.StringTypeEnum;
import com.sportbetapp.domain.type.typed.TypeEnum;

public enum GoalsDirection implements StringTypeEnum {

    SCORED("scored"),
    MISSED("missed"),
    MORE_THAN("more than"),
    LESS_THAN("less than"),
    NONE(EMPTY);

    String value;

    GoalsDirection(String value) {
        this.value = value;
    }


    @Override
    public String getValue() {
        return value;
    }

    public static GoalsDirection of(String value) {
        return TypeEnum.findEnumValueOrThrowException(GoalsDirection.class, value);
    }
}
