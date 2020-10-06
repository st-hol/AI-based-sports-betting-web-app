package com.sportbetapp.domain.type;

import com.sportbetapp.domain.type.typed.StringTypeEnum;
import com.sportbetapp.domain.type.typed.TypeEnum;

public enum FieldRelation implements StringTypeEnum {

    HOME("home"),
    AWAY("away");

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
