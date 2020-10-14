package com.sportbetapp.domain.type;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.sportbetapp.domain.type.typed.StringTypeEnum;
import com.sportbetapp.domain.type.typed.TypeEnum;

public enum FieldRelation implements StringTypeEnum {

    HOME("home"),
    AWAY("away"),
    NONE(EMPTY);

    String value;

    FieldRelation(String value) {
        this.value = value;
    }


    @Override
    public String getValue() {
        return value;
    }

    public static FieldRelation of(String value) {
        return TypeEnum.findEnumValueOrThrowException(FieldRelation.class, value);
    }
}
