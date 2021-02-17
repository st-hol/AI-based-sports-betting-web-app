package com.sportbetapp.domain.type;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sportbetapp.domain.type.typed.StringTypeEnum;

public enum BetType implements StringTypeEnum {

    WINNER("Winner", 1.5),
    EXACT_GAME_SCORE("Exact game score", 3),
    BOTH_WILL_SCORE_AT_LEAST_BY_ONE_HIT("Both will score at least by one hit", 0.5),
    BOTH_WILL_NOT_SCORE_ANY_GOALS("Both will not score any hits", 0.5),
    GOALS_BY_TEAM("Hits by team", 2),
    MISSES_BY_TEAM("Misses by team", 2),
    GOALS_MORE_THAN("Hits more than", 0.5),
    MISSES_MORE_THAN("Misses more than", 0.5);

    public static Map<String, Double> typeToCoef = Stream.of(values())
                    .collect(Collectors.toMap(k -> k.value, v -> v.coefficient));

    private String value;
    private double coefficient;

    BetType(String value, double coefficient) {
        this.value = value;
        this.coefficient = coefficient;
    }

    public double getCoefficient() {
        return coefficient;
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
