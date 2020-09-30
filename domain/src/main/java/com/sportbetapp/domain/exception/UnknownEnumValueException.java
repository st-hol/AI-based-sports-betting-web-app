package com.sportbetapp.domain.exception;

public class UnknownEnumValueException extends IllegalArgumentException {

    public UnknownEnumValueException(String enumType, String unknownEnumValue) {
        super(String.format("Enum %s: unknown value %s", enumType, unknownEnumValue));
    }
}
