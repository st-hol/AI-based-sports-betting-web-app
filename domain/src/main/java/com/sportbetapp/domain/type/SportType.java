package com.sportbetapp.domain.type;

import com.sportbetapp.domain.type.typed.StringTypeEnum;
import com.sportbetapp.domain.type.typed.TypeEnum;

public enum SportType implements StringTypeEnum {
    FOOTBALL("Football"),
    BASKETBALL("Basketball"),
    VOLLEYBALL("Volleyball"),
    BASEBALL("Baseball"),
    HOCKEY("Hockey"),
    TENNIS("Tennis"),
    TABLE_TENNIS("Table tennis");

    private String value;

    SportType(String value) {
        this.value = value;
    }

    public static SportType of(String value) {
        return TypeEnum.findEnumValueOrThrowException(SportType.class, value);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equalsTo(String value) {
        return this.value.equals(value);
    }
}
