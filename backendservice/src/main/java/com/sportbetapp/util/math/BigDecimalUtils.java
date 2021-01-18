package com.sportbetapp.util.math;

import static java.math.BigInteger.ZERO;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.CollectionUtils;

import com.sportbetapp.util.Utils;

public final class BigDecimalUtils {

    public static final BigDecimal BIG_DECIMAL_100 = BigDecimal.valueOf(100);
    public static final BigDecimal MAX_AMOUNT = BigDecimal.valueOf(9999.99);
    public static final BigDecimal DECIMAL_5_2_MAX = BigDecimal.valueOf(99999.99);
    public static final BigDecimal DECIMAL_5_2_MIN = BigDecimal.valueOf(-99999.99);
    public static final BigDecimal DECIMAL_5_1_MAX = BigDecimal.valueOf(9999.9);
    public static final BigDecimal DECIMAL_5_1_MIN = BigDecimal.valueOf(-9999.9);
    public static final BigDecimal DECIMAL_3_1_MAX = BigDecimal.valueOf(99.9);
    public static final BigDecimal DECIMAL_3_1_MIN = BigDecimal.valueOf(-99.9);
    public static final BigDecimal ZERO_SCALE_TWO = new BigDecimal("0.00");

    private BigDecimalUtils() {
    }

    public static BigDecimal valueOrZero(BigDecimal value) {
        return ObjectUtils.defaultIfNull(value, BigDecimal.ZERO);
    }

    public static boolean isZero(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) == 0;
    }

    public static boolean isNonZero(BigDecimal value) {
        return !isZero(value);
    }

    public static BigDecimal sumBigDecimals(List<BigDecimal> values) {
        return sumBigDecimalsSafe(values, 0, values.size());
    }

    public static BigDecimal sumBigDecimals(BigDecimal... values) {
        return sumBigDecimalsSafe(Arrays.asList(values), 0, values.length);
    }

    /**
     * Sum value from start to end indexes. If start < end then sum values from start to last element and from first
     * element to end.
     *
     * @param values list of values
     * @param start  start index inclusive
     * @param end    end index exclusive
     * @return sum of values based on start and end indexes.
     */
    public static BigDecimal sumBigDecimalsSafe(List<BigDecimal> values, int start, int end) {
        if (CollectionUtils.isEmpty(values)) {
            return BigDecimal.ZERO;
        }

        int size = values.size();
        int safeStart = safeIndex(size, start);
        int safeEnd = safeIndex(size, end);
        return sumBigDecimals(values, safeStart, safeEnd);
    }

    public static BigDecimal sumBigDecimals(List<BigDecimal> values, int start, int end) {
        BigDecimal sum;

        if (start == end || values.isEmpty()) {
            sum = BigDecimal.ZERO;
        } else if (start < end) {
            sum = values.subList(start, end).stream()
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            sum = sumBigDecimals(values, 0, end);
            int size = values.size();
            if (start < size) {
                sum = sumBigDecimals(values, start, size).add(sum);
            }
        }

        return sum;
    }

    private static int safeIndex(int size, int index) {
        if (index < 0) {
            return 0;
        } else if (index > size) {
            return size;
        }
        return index;
    }

    public static BigDecimal cropIntToDecimal5Point2(BigDecimal value) {
        return Utils.cropValue(value, DECIMAL_5_2_MIN, DECIMAL_5_2_MAX);
    }

    public static BigDecimal cropIntToDecimal5Point1(BigDecimal value) {
        return Utils.cropValue(value, DECIMAL_5_1_MIN, DECIMAL_5_1_MAX);
    }

    public static BigDecimal cropIntToDecimal3Point1(BigDecimal value) {
        return Utils.cropValue(value, DECIMAL_3_1_MIN, DECIMAL_3_1_MAX);
    }

    public static BigDecimal findThePercentageDifferenceOfNumbers(BigDecimal num1, BigDecimal num2) {
        if (BigDecimalUtils.isNullOrEqualsZero(num1) || num2 == null) {
            return BigDecimal.ZERO;
        }

        return num1.subtract(num2)
                .divide(num1, 10, RoundingMode.DOWN)
                .multiply(BigDecimal.valueOf(100));
    }

    public static boolean isLessOrEqualsZero(BigDecimal value) {
        return Utils.isLessOrEquals(value, BigDecimal.ZERO);
    }

    public static boolean isLessThan(BigDecimal first, BigDecimal second) {
        return first != null && second != null && first.compareTo(second) < 0;
    }

    public static BigDecimal toBigDecimalOrZero(BigDecimal val) {
        return val == null ? BigDecimal.ZERO : val;
    }

    public static BigDecimal toBigDecimalOrZero(Integer val) {
        return toBigDecimalOrDefault(val, BigDecimal.ZERO);
    }

    public static BigDecimal toBigDecimalOrDefault(Integer val, BigDecimal defaultValue) {
        return val != null ? BigDecimal.valueOf(val) : defaultValue;
    }

    public static BigDecimal toBigDecimalOrDefault(Double val, BigDecimal defaultValue) {
        return val != null ? BigDecimal.valueOf(val) : defaultValue;
    }

    public static boolean isLessThanZero(BigDecimal value) {
        return isLessThan(value, BigDecimal.ZERO);
    }

    public static boolean isGreaterOrEqualsThan(BigDecimal first, BigDecimal second) {
        return first != null && second != null && first.compareTo(second) >= 0;
    }

    public static boolean isGreaterOrEqualsThanInt(BigDecimal first, Integer second) {
        return second != null && isGreaterOrEqualsThan(first, BigDecimal.valueOf(second));
    }

    public static Integer extractDigitsFromRight(BigDecimal scaledValue, int precision) {
        if (scaledValue == null) {
            return 0;
        }

        String wrk = scaledValue.toString();
        int end = wrk.length();
        int beginIndex = Math.max(end - precision, 0);
        return Integer.valueOf(wrk.substring(beginIndex, end));
    }

    public static BigInteger extractBigIntegerDigitsFromRight(BigDecimal scaledValue, int precision) {
        if (scaledValue == null) {
            return ZERO;
        }

        String wrk = scaledValue.toString();
        int end = wrk.length();
        int beginIndex = Math.max(end - precision, 0);
        String stringRes = wrk.substring(beginIndex, end);
        return stringRes.length() == 0 ? ZERO : new BigInteger(stringRes);
    }

    private static BigDecimal scaleAndNegate(int scale, BigInteger resultValue, boolean negate) {
        BigDecimal value = new BigDecimal(resultValue);
        if (negate) {
            return value.abs().scaleByPowerOfTen(scale).negate();
        } else {
            return value.scaleByPowerOfTen(scale);
        }
    }

    public static boolean isGreaterOrEqualsZero(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) > -1;
    }


    public static BigDecimal divideAbyBDefault(BigDecimal a, BigDecimal b, RoundingMode roundingMode) {
        return valueOrZero(a).divide(valueOrZero(b), roundingMode);
    }

    public static BigDecimal divideAbyB(BigDecimal a, BigDecimal b, MathContext mathContext) {
        return valueOrZero(a).divide(valueOrZero(b), mathContext);
    }

    public static BigDecimal divideAbyB(BigDecimal a, BigDecimal b) {
        return divideAbyB(a, b, Utils.NUMBER_OF_DECIMALS);
    }

    public static BigDecimal divideAbyB(BigDecimal a, BigDecimal b, int scale) {
        return divideAbyB(a, b, scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal divideAbyB(BigDecimal a, BigDecimal b, int scale, RoundingMode mode) {
        return isNullOrEqualsZero(a) || isNullOrEqualsZero(b) ? BigDecimal.ZERO : a.divide(b, scale, mode);
    }

    public static BigDecimal divideWithScale(BigDecimal a, BigDecimal b, int scale) {
        return divideAbyB(a, b, scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal divideWithScale(Integer a, Integer b, int scale) {
        return divideWithScale(a, b, scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal divideWithScale(Long a, Integer b, int scale) {
        if (a == null || a.equals(0L) || b == null || b.equals(0)) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(a).divide(BigDecimal.valueOf(b), scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal divideWithScale(Integer a, Integer b, int scale, RoundingMode roundingMode) {
        if (a == null || a.equals(0) || b == null || b.equals(0)) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(a).divide(BigDecimal.valueOf(b), scale, roundingMode);
    }

    public static BigDecimal divideWithScale(Long a, Long b, int scale) {
        if (a == null || a.equals(0L) || b == null || b.equals(0L)) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(a).divide(BigDecimal.valueOf(b), scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal divideAsBigDecimals(Long a, Long b) {
        if (Utils.isZeroOrNull(a) || Utils.isZeroOrNull(b)) {
            return BigDecimal.ZERO;
        }
        return divide(BigDecimal.valueOf(a), BigDecimal.valueOf(b));
    }

    public static BigDecimal divideAsBigDecimals(Integer a, Integer b) {
        if (a == null || a.equals(0) || b == null || b.equals(0)) {
            return BigDecimal.ZERO;
        }
        return divide(BigDecimal.valueOf(a), BigDecimal.valueOf(b));
    }

    private static BigDecimal divide(BigDecimal a, BigDecimal b) {
        return a.divide(b, Utils.NUMBER_OF_DECIMALS, RoundingMode.HALF_UP);
    }

    public static BigDecimal getMaxOrValue(BigDecimal value) {
        return Utils.isGreaterThan(value, MAX_AMOUNT)
                ? MAX_AMOUNT : value;
    }

    public static boolean isNullableGreaterOrEqualsThan(BigDecimal first, BigDecimal second) {
        return (first == null && second == null) || isGreaterOrEqualsThan(first, second);
    }

    public static boolean isNullOrLessEqualZero(BigDecimal value) {
        return BigDecimalUtils.isNullOrEqualsZero(value) || isLessThanZero(value);
    }

    public static BigDecimal round(BigDecimal value, int places) {
        BigDecimal number = ObjectUtils.defaultIfNull(value, BigDecimal.ZERO);
        return number.setScale(places, RoundingMode.HALF_UP);
    }

    public static BigDecimal round(BigDecimal value, int places, RoundingMode roundingMode) {
        BigDecimal number = ObjectUtils.defaultIfNull(value, BigDecimal.ZERO);
        return number.setScale(places, roundingMode);
    }

    public static int roundToInt(BigDecimal value) {
        return round(value, 0).intValue();
    }

    public static boolean isGreaterThan(BigDecimal first, BigDecimal second) {
        return first != null && second != null && first.compareTo(second) > 0;
    }

    public static boolean isGreaterThanInt(BigDecimal first, Integer second) {
        return second != null && isGreaterThan(first, BigDecimal.valueOf(second));
    }

    public static boolean isGreaterThanZero(BigDecimal value) {
        return isGreaterThan(value, BigDecimal.ZERO);
    }

    public static boolean isEquals(BigDecimal first, BigDecimal second) {
        return first != null && second != null && first.compareTo(second) == 0;
    }

    public static boolean isEqualsZero(BigDecimal first) {
        return BigDecimalUtils.isEquals(first, BigDecimal.ZERO);
    }

    public static boolean isNullOrEqualsZero(BigDecimal number) {
        return number == null || number.compareTo(BigDecimal.ZERO) == 0;
    }

    public static boolean isNotNullAndNotEqualsZero(BigDecimal number) {
        return !BigDecimalUtils.isNullOrEqualsZero(number);
    }

    public static boolean isIntegerValue(BigDecimal bd) {
        return bd != null && (bd.signum() == 0 || bd.scale() <= 0 || bd.stripTrailingZeros().scale() <= 0);
    }

    public static BigDecimal defaultZeroScaleTwo(BigDecimal value) {
        return ObjectUtils.defaultIfNull(value, ZERO_SCALE_TWO);
    }

    public static BigDecimal add(BigDecimal number1, BigDecimal number2) {
        if (number1 == null && number2 == null) {
            return BigDecimal.ZERO;
        } else if (number1 == null) {
            return number2;
        } else if (number2 == null) {
            return number1;
        } else {
            return number1.add(number2);
        }
    }

    public static BigDecimal add(BigDecimal... numbers) {
        if (numbers == null || numbers.length == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal result = numbers[0] != null ? numbers[0] : BigDecimal.ZERO;
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] != null) {
                result = result.add(numbers[i]);
            }
        }

        return result;
    }

    public static BigDecimal subtract(BigDecimal number1, BigDecimal number2) {
        if (number1 == null && number2 == null) {
            return BigDecimal.ZERO;
        } else if (number1 == null) {
            return number2;
        } else if (number2 == null) {
            return number1;
        } else {
            return number1.subtract(number2);
        }
    }

    public static BigDecimal subtract(BigDecimal... numbers) {
        if (numbers == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal result = null;
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] != null) {
                result = result != null ? result.subtract(numbers[i]) : numbers[i];
            }
        }
        if (result == null) {
            result = BigDecimal.ZERO;
        }

        return result;
    }

    public static BigDecimal subtractInt(BigDecimal number, Integer value) {

        return subtract(number, value == null ? null : BigDecimal.valueOf(value));
    }

    public static BigDecimal subtractIntInt(Integer number, Integer value) {
        return subtractInt(number == null ? null : BigDecimal.valueOf(number), value);
    }

    public static BigDecimal multiply(BigDecimal number1, BigDecimal number2) {
        if (number1 == null || number2 == null ||
                number1.compareTo(BigDecimal.ZERO) == 0 || number2.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return number1.multiply(number2);
    }

    public static BigDecimal multiply(BigDecimal number1, BigDecimal number2, BigDecimal number3) {
        return multiply(multiply(number1, number2), number3);
    }

    public static BigDecimal multiply(BigDecimal a, BigDecimal b, MathContext mathContext) {
        return valueOrZero(a).multiply(valueOrZero(b), mathContext);
    }

    public static BigDecimal multiply(int scale, RoundingMode roundingMode, BigDecimal number1, BigDecimal number2) {
        return multiply(number1, number2).setScale(scale, roundingMode);
    }

    public static BigDecimal multiply(int scale, RoundingMode roundingMode, BigDecimal number1, BigDecimal number2,
                                      BigDecimal number3) {
        return multiply(number1, number2, number3).setScale(scale, roundingMode);
    }

    public static boolean isLessOrEqualsThan(BigDecimal first, BigDecimal second) {
        return Utils.isLessOrEquals(first, second);
    }

    public static BigDecimal min(BigDecimal first, BigDecimal second) {
        return valueOrZero(first).min(valueOrZero(second));
    }

    public static BigDecimal remainder(BigDecimal a, BigDecimal b) {
        if (Utils.isEquals(a, BigDecimal.ZERO) || Utils.isEquals(b, BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }

        return a.remainder(b);
    }

    public static BigDecimal setScale(BigDecimal a, int scale, RoundingMode roundingMode) {
        return a == null ? BigDecimal.ZERO : a.setScale(scale, roundingMode);
    }

    public static BigDecimal max(BigDecimal first, BigDecimal second) {
        return valueOrZero(first).max(valueOrZero(second));
    }

    public static BigDecimal remainderWithContext(BigDecimal divident, BigDecimal divisor, MathContext mathContext) {
        return valueOrZero(divident).remainder(valueOrZero(divisor), mathContext);
    }

    // L2L implementation: trim more than 2 left digits, like 12345.67 => 45.67
    public static BigDecimal trimMoreThan2LeftDigits(BigDecimal valueToCrop) {
        return valueToCrop.remainder(BIG_DECIMAL_100);
    }

    public static BigDecimal defaultIfNullOrZero(BigDecimal val, BigDecimal defaultValue) {
        return isNullOrEqualsZero(val) ? valueOrZero(defaultValue) : val;
    }

}
