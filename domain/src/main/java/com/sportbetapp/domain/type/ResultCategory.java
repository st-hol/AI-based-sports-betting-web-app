package com.sportbetapp.domain.type;


import com.sportbetapp.domain.type.typed.StringTypeEnum;
import com.sportbetapp.domain.type.typed.TypeEnum;

public enum ResultCategory implements StringTypeEnum {
    WIN("win"), DRAW("draw"), LOSS("loss");

    String value;

    ResultCategory(String value) {
        this.value = value;
    }


    @Override
    public String getValue() {
        return value;
    }

    public static ResultCategory of(String value) {
        return TypeEnum.findEnumValueOrThrowException(ResultCategory.class, value);
    }

}
