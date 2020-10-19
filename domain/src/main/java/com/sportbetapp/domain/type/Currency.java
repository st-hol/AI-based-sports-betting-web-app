package com.sportbetapp.domain.type;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.sportbetapp.domain.type.typed.StringTypeEnum;
import com.sportbetapp.domain.type.typed.TypeEnum;

public enum Currency  implements StringTypeEnum {

    UAH("UAH"),
    EUR("EUR"),
    USD("USD");

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