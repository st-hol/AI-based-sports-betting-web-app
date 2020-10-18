package com.sportbetapp.domain.type;

import com.sportbetapp.domain.type.typed.StringTypeEnum;
import com.sportbetapp.domain.type.typed.TypeEnum;

public enum OutcomeType implements StringTypeEnum {

    SUCCESS("success"),
    FAILURE("failure"),
    AWAITING("awaiting");

    String value;

    OutcomeType(String value) {
        this.value = value;
    }


    @Override
    public String getValue() {
        return value;
    }

    public static OutcomeType of(String value) {
        return TypeEnum.findEnumValueOrThrowException(OutcomeType.class, value);
    }
}
