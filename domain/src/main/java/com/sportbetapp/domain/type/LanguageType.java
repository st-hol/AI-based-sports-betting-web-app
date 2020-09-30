package com.sportbetapp.domain.type;


import java.util.stream.Stream;

import com.sportbetapp.domain.type.typed.StringTypeEnum;
import com.sportbetapp.domain.type.typed.TypeEnum;

public enum LanguageType implements StringTypeEnum {

    UA("UA"),
    EN("EN");


    private final String value;

    LanguageType(String value) {
        this.value = value;
    }

    /**
     * default - EN
     **/
    public static LanguageType fromValue(String value) {
        return Stream.of(values()).filter(v -> v.getValue().equals(value)).findFirst().orElse(EN);
    }

    public static LanguageType of(String value) {
        return TypeEnum.findEnumValueOrThrowException(LanguageType.class, value);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equalsTo(String value) {
        return false;
    }


}