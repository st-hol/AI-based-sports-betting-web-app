package com.sportbetapp.domain.type;

import com.sportbetapp.domain.type.typed.StringTypeEnum;
import com.sportbetapp.domain.type.typed.TypeEnum;

public enum Currency  implements StringTypeEnum {

    POINTS("Points");

    String value;

    Currency(String value) {
        this.value = value;
    }


    @Override
    public String getValue() {
        return value;
    }

    public static Currency of(String value) {
        return TypeEnum.findEnumValueOrThrowException(Currency.class, value);
    }
}
