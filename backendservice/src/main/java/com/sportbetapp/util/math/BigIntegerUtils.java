package com.sportbetapp.util.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;

import com.sportbetapp.util.Utils;

/**
 * Utilities methods in order to perform operation with {@link BigIntegerUtils} objects.
 * Methods in this class must be null-safe.
 */
@SuppressWarnings({"frontier-java:MathOnBigNumbers", "frontier-java:AvoidToString", "frontier-java:MathOnBoxedNumbers"})
public final class BigIntegerUtils {

    private BigIntegerUtils() {
    }

    public static boolean isZero(BigInteger value) {
        return value != null && value.compareTo(BigInteger.ZERO) == 0;
    }

    public static boolean isGreaterThanZero(BigInteger value) {
        return value != null && value.signum() > 0;
    }

    public static Integer toIntOrDefault(BigInteger bi, Integer defaultValue) {
        return Utils.mapValueOrDefault(bi, BigInteger::intValue, defaultValue);
    }

    public static BigInteger toBigIntOrDefault(Integer val, BigInteger defaultValue) {
        return val != null ? BigInteger.valueOf(val) : defaultValue;
    }

    public static BigInteger[] divideAndRemainder(BigInteger first, BigInteger second) {
        return valueOrZero(first).divideAndRemainder(valueOrZero(second));
    }

    public static BigInteger divide(BigInteger value, Integer divisor, RoundingMode roundingMode) {
        if (BigIntegerUtils.isZeroOrNull(value) || Utils.isZeroOrNull(divisor)) {
            return BigInteger.ZERO;
        }
        return new BigDecimal(value).divide(BigDecimal.valueOf(divisor), roundingMode).toBigIntegerExact();
    }

    public static BigDecimal divide(BigInteger value, Integer divisor) {
        if (BigIntegerUtils.isZeroOrNull(value) || Utils.isZeroOrNull(divisor)) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(value).divide(BigDecimal.valueOf(divisor), MathContext.DECIMAL64).setScale(2,
                RoundingMode.HALF_UP);
    }

    public static BigInteger sanitizeSearchNumber(BigInteger value) {
        return value == null || value.equals(BigInteger.ZERO) ? null : value;
    }

    public static boolean isZeroOrNull(BigInteger value) {
        return value == null || BigInteger.ZERO.equals(value);
    }

    public static BigInteger add(BigInteger... numbers) {
        if (numbers == null) {
            return BigInteger.ZERO;
        }
        return Arrays.stream(numbers)
                .filter(Objects::nonNull)
                .reduce(BigInteger.ZERO, BigInteger::add);
    }

    public static BigInteger subtract(BigInteger... numbers) {
        if (numbers == null) {
            return BigInteger.ZERO;
        }
        return Arrays.stream(numbers)
                .filter(Objects::nonNull)
                .reduce(BigInteger::subtract).orElse(BigInteger.ZERO);
    }

    public static BigInteger multiply(BigInteger... numbers) {
        if (numbers == null) {
            return BigInteger.ZERO;
        }
        return Arrays.stream(numbers)
                .map(BigIntegerUtils::toBigIntegerOrZero)
                .reduce(BigInteger::multiply).orElse(BigInteger.ZERO);
    }

    public static BigInteger toBigIntegerOrZero(BigInteger val) {
        return val == null ? BigInteger.ZERO : val;
    }

    public static boolean equals(BigInteger a, BigInteger b) {
        return a != null && b != null && a.equals(b);
    }

    public static boolean isGreaterThan(BigInteger first, BigInteger second) {
        return first != null && second != null && first.compareTo(second) > 0;
    }

    public static boolean isGreaterThanOrEquals(BigInteger first, BigInteger second) {
        return first != null && second != null && first.compareTo(second) >= 0;
    }

    public static boolean isLessThan(BigInteger first, BigInteger second) {
        return first != null && second != null && first.compareTo(second) < 0;
    }

    public static boolean isLessThanOrEquals(BigInteger first, BigInteger second) {
        return first != null && second != null && first.compareTo(second) <= 0;
    }

    public static BigInteger max(BigInteger first, BigInteger second) {
        return valueOrZero(first).max(valueOrZero(second));
    }

    public static BigInteger valueOrZero(BigInteger value) {
        return ObjectUtils.defaultIfNull(value, BigInteger.ZERO);
    }
}
