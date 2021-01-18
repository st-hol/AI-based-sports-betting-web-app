package com.sportbetapp.util.math;

import static java.math.RoundingMode.UP;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.sportbetapp.util.math.BigDecimalUtils.extractDigitsFromRight;
import static com.sportbetapp.util.math.BigDecimalUtils.isEqualsZero;
import static com.sportbetapp.util.math.BigDecimalUtils.isGreaterThan;
import static com.sportbetapp.util.math.BigDecimalUtils.isGreaterThanInt;
import static com.sportbetapp.util.math.BigDecimalUtils.isGreaterThanZero;
import static com.sportbetapp.util.math.BigDecimalUtils.isIntegerValue;
import static com.sportbetapp.util.math.BigDecimalUtils.isLessOrEqualsZero;
import static com.sportbetapp.util.math.BigDecimalUtils.isLessThan;
import static com.sportbetapp.util.math.BigDecimalUtils.toBigDecimalOrDefault;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.sportbetapp.util.Utils;


public class BigDecimalUtilsTest {

    private static final BigDecimal BIG_DEC_FOUR_POINT_SEVEN = BigDecimal.valueOf(4.7);
    private static final BigDecimal BIG_DEC_THREE = BigDecimal.valueOf(3);
    private static final BigDecimal BIG_DEC_THIRTY = BigDecimal.valueOf(30);
    private static final BigDecimal BIG_DEC_ONE_POINT_EIGHT = BigDecimal.valueOf(1.8);

    private static final int NEGATIVE_NINE = -9;
    private static final int ZERO = 0;
    private static final int ONE = 1;
    private static final int TEN = 10;


    @Test
    public void sumBigDecimals_checkSum() {
        List<BigDecimal> list = new ArrayList<>(Collections.nCopies(4, new BigDecimal(1)));
        BigDecimal result = BigDecimalUtils.sumBigDecimals(list, 0, 4);
        assertThat(result, is(new BigDecimal(4)));
    }

    @Test
    public void sumBigDecimalsWithListParam_checkSum() {
        List<BigDecimal> list = new ArrayList<>(Collections.nCopies(4, new BigDecimal(1)));
        BigDecimal result = BigDecimalUtils.sumBigDecimals(list);
        assertThat(result, is(new BigDecimal(4)));
    }

    @Test
    public void sumBigDecimalsWithVarargParam_checkSum() {
        BigDecimal result = BigDecimalUtils.sumBigDecimals(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
                BigDecimal.ONE);
        assertThat(result, is(new BigDecimal(4)));
    }

    @Test
    public void sumBigDecimalsSafe_checkEmptyLisit() {
        assertSafeSum(0, 1, 2, BigDecimal.ZERO);
    }

    @Test
    public void sumBigDecimalsSafe_checkStartNegative() {
        assertSafeSum(4, -2, 2, new BigDecimal(2));
    }

    @Test
    public void sumBigDecimalsSafe_checkStartGreaterThenSize() {
        assertSafeSum(4, 1, TEN, new BigDecimal(3));
    }

    @Test
    public void isNonZeroBigDecimal() {
        assertFalse(BigDecimalUtils.isNonZero(BigDecimal.ZERO));
        assertFalse(BigDecimalUtils.isNonZero(new BigDecimal("0.00000")));

        assertTrue(BigDecimalUtils.isNonZero(null));

        assertTrue(BigDecimalUtils.isNonZero(new BigDecimal("0.001")));
        assertTrue(BigDecimalUtils.isNonZero(new BigDecimal("1")));
        assertTrue(BigDecimalUtils.isNonZero(BigDecimal.valueOf(10L)));
    }

    @Test
    public void cropIntToDecimal7Point2() {
        assertThat(BigDecimalUtils.cropIntToDecimal5Point2(BigDecimal.valueOf(123456789)),
                is(BigDecimalUtils.DECIMAL_5_2_MAX));
        assertThat(BigDecimalUtils.cropIntToDecimal5Point2(BigDecimal.valueOf(1234.51)), is(BigDecimal.valueOf(1234.51)));
        assertThat(BigDecimalUtils.cropIntToDecimal5Point2(BigDecimal.valueOf(-123456789)),
                is(BigDecimalUtils.DECIMAL_5_2_MIN));
    }

    @Test
    public void cropIntToDecimal5Point1() {
        assertThat(BigDecimalUtils.cropIntToDecimal5Point1(BigDecimal.valueOf(12345.6)),
                is(BigDecimalUtils.DECIMAL_5_1_MAX));
        assertThat(BigDecimalUtils.cropIntToDecimal5Point1(BigDecimal.valueOf(1234.5)), is(BigDecimal.valueOf(1234.5)));
        assertThat(BigDecimalUtils.cropIntToDecimal5Point1(BigDecimal.valueOf(-12345.6)),
                is(BigDecimalUtils.DECIMAL_5_1_MIN));
    }

    @Test
    public void cropIntToDecimal3Point1() {
        assertThat(BigDecimalUtils.cropIntToDecimal3Point1(BigDecimal.valueOf(12345.6)),
                is(BigDecimalUtils.DECIMAL_3_1_MAX));
        assertThat(BigDecimalUtils.cropIntToDecimal3Point1(BigDecimal.valueOf(34.5)), is(BigDecimal.valueOf(34.5)));
        assertThat(BigDecimalUtils.cropIntToDecimal3Point1(BigDecimal.valueOf(-12345.6)),
                is(BigDecimalUtils.DECIMAL_3_1_MIN));
    }

    @Test
    public void findThePercentageDifferenceOfNumbers() {
        BigDecimal num1 = new BigDecimal(500);
        BigDecimal num2 = new BigDecimal(250);
        assertThat("Should return correct result",
                BigDecimalUtils.findThePercentageDifferenceOfNumbers(num1, num2).doubleValue(),
                is(50.0));

        assertThat("Should return zero",
                BigDecimalUtils.findThePercentageDifferenceOfNumbers(null, null).doubleValue(),
                is(0.0));

        assertThat("Should return zero",
                BigDecimalUtils.findThePercentageDifferenceOfNumbers(BigDecimal.ZERO, BigDecimal.ZERO).doubleValue(),
                is(0.0));
    }

    @Test
    public void bigDecimalHelperMethods() {
        assertTrue(isLessOrEqualsZero(BigDecimal.valueOf(-1D)));
        assertTrue(isLessOrEqualsZero(BigDecimal.ZERO));
        assertFalse(isLessOrEqualsZero(BigDecimal.ONE));
        assertFalse(isLessOrEqualsZero(null));
        assertFalse(isLessOrEqualsZero(null));

        assertTrue(isLessThan(BigDecimal.ONE, BigDecimal.TEN));
        assertFalse(isLessThan(BigDecimal.ONE, BigDecimal.ONE));
        assertFalse(isLessThan(BigDecimal.TEN, BigDecimal.ONE));
        assertFalse(isLessThan(null, BigDecimal.TEN));
        assertFalse(isLessThan(BigDecimal.TEN, null));

        assertTrue(isGreaterThan(BigDecimal.TEN, BigDecimal.ONE));
        assertFalse(isGreaterThan(BigDecimal.ONE, BigDecimal.ONE));
        assertFalse(isGreaterThan(BigDecimal.ONE, BigDecimal.TEN));
        assertFalse(isGreaterThan(null, BigDecimal.TEN));
        assertFalse(isGreaterThan(BigDecimal.TEN, null));

        assertFalse(isGreaterThanInt(BigDecimal.TEN, null));
        assertTrue(isGreaterThanInt(BigDecimal.TEN, Integer.valueOf(ONE)));
        assertFalse(isGreaterThanInt(BigDecimal.ONE, Integer.valueOf(ONE)));
        assertFalse(isGreaterThanInt(BigDecimal.ONE, Integer.valueOf(TEN)));
        assertFalse(isGreaterThanInt(null, Integer.valueOf(TEN)));

        assertTrue(isGreaterThanZero(BigDecimal.ONE));
        assertFalse(isGreaterThanZero(BigDecimal.valueOf(-1D)));
        assertFalse(isGreaterThanZero(BigDecimal.ZERO));

        assertTrue(isEqualsZero(BigDecimal.ZERO));
        assertFalse(isEqualsZero(BigDecimal.ONE));
        assertFalse(isEqualsZero(null));
    }

    @Test
    public void toBigDecimalOrZero() {
        Integer nil = null;

        assertThat(BigDecimalUtils.toBigDecimalOrZero(nil), is(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.toBigDecimalOrZero(0), is(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.toBigDecimalOrZero(12), is(BigDecimal.valueOf(12L)));
        assertThat(BigDecimalUtils.toBigDecimalOrZero(-13), is(BigDecimal.valueOf(-13L)));
        assertThat(BigDecimalUtils.toBigDecimalOrZero(Integer.MAX_VALUE), is(BigDecimal.valueOf(Integer.MAX_VALUE)));
        assertThat(BigDecimalUtils.toBigDecimalOrZero(Integer.MIN_VALUE), is(BigDecimal.valueOf(Integer.MIN_VALUE)));
    }

    @Test
    public void toBigDecimalOrDefault_norm_double() {
        Double value = null;

        assertThat(toBigDecimalOrDefault(0, null), is(BigDecimal.ZERO));
        assertThat(toBigDecimalOrDefault(Double.MAX_VALUE, null), is(BigDecimal.valueOf(Double.MAX_VALUE)));
        assertThat(toBigDecimalOrDefault(Double.MIN_VALUE, null), is(BigDecimal.valueOf(Double.MIN_VALUE)));
        assertThat(toBigDecimalOrDefault(value, BigDecimal.ZERO), is(BigDecimal.ZERO));
        assertThat(toBigDecimalOrDefault(value, null), nullValue());
    }

    @Test
    public void toBigDecimalOrDefault_norm() {
        Integer value = null;

        assertThat(toBigDecimalOrDefault(0, null), is(BigDecimal.ZERO));
        assertThat(toBigDecimalOrDefault(Integer.MAX_VALUE, null), is(BigDecimal.valueOf(Integer.MAX_VALUE)));
        assertThat(toBigDecimalOrDefault(Integer.MIN_VALUE, null), is(BigDecimal.valueOf(Integer.MIN_VALUE)));
        assertThat(toBigDecimalOrDefault(value, BigDecimal.ZERO), is(BigDecimal.ZERO));
        assertThat(toBigDecimalOrDefault(value, null), nullValue());
    }

    @Test
    public void isGreaterOrEqualsThan() {
        boolean isEquals = BigDecimalUtils.isGreaterOrEqualsThan(new BigDecimal(11), null);
        assertFalse(isEquals);

        isEquals = BigDecimalUtils.isGreaterOrEqualsThan(null, new BigDecimal(11));
        assertFalse(isEquals);

        isEquals = BigDecimalUtils.isGreaterOrEqualsThan(new BigDecimal(11), new BigDecimal(11.1));
        assertFalse(isEquals);

        isEquals = BigDecimalUtils.isGreaterOrEqualsThan(new BigDecimal(11.1), new BigDecimal(11.1));
        assertTrue(isEquals);

        isEquals = BigDecimalUtils.isNullableGreaterOrEqualsThan(null, null);
        assertTrue(isEquals);
    }

    @Test
    public void isGreaterOrEqualsThanInt() {
        boolean isEquals = BigDecimalUtils.isGreaterOrEqualsThanInt(new BigDecimal(11), null);
        assertFalse(isEquals);

        isEquals = BigDecimalUtils.isGreaterOrEqualsThanInt(null, Integer.valueOf(TEN));
        assertFalse(isEquals);

        isEquals = BigDecimalUtils.isGreaterOrEqualsThanInt(new BigDecimal(ONE), Integer.valueOf(TEN));
        assertFalse(isEquals);

        isEquals = BigDecimalUtils.isGreaterOrEqualsThanInt(new BigDecimal(TEN), Integer.valueOf(TEN));
        assertTrue(isEquals);

        isEquals = BigDecimalUtils.isGreaterOrEqualsThanInt(null, null);
        assertFalse(isEquals);
    }

    @Test
    public void isGreaterOrEqualsZero() {
        assertThat(BigDecimalUtils.isGreaterOrEqualsZero(null), is(false));
        assertThat(BigDecimalUtils.isGreaterOrEqualsZero(BigDecimal.valueOf(-5)), is(false));
        assertThat(BigDecimalUtils.isGreaterOrEqualsZero(BigDecimal.ZERO), is(true));
        assertThat(BigDecimalUtils.isGreaterOrEqualsZero(BigDecimal.TEN), is(true));
    }

    @Test
    public void devisionByZeroTest() {
        BigDecimal value12 = new BigDecimal(12);

        assertThat(BigDecimalUtils.divideAbyB(BigDecimal.ZERO, value12), is(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.divideAbyB(null, value12), is(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.divideAbyB(BigDecimal.ZERO, null), is(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.divideAbyB(value12, BigDecimal.ZERO), is(BigDecimal.ZERO));
    }

    @Test
    public void correctDevisionTest() {
        BigDecimal value3 = new BigDecimal(3);
        BigDecimal correctRes = new BigDecimal(3.33);
        correctRes = correctRes.setScale(2, RoundingMode.HALF_UP);

        assertThat(BigDecimalUtils.divideAbyB(BigDecimal.TEN, value3), is(correctRes));
    }

    @Test
    public void devisionByZeroTestScaleAndMode() {
        BigDecimal value12 = new BigDecimal(12);

        assertThat(BigDecimalUtils.divideAbyB(BigDecimal.ZERO, value12, Utils.NUMBER_OF_DECIMALS, RoundingMode.HALF_UP),
                is(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.divideAbyB(null, value12, Utils.NUMBER_OF_DECIMALS, RoundingMode.HALF_UP),
                is(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.divideAbyB(BigDecimal.ZERO, null, Utils.NUMBER_OF_DECIMALS, RoundingMode.HALF_UP),
                is(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.divideAbyB(value12, BigDecimal.ZERO, Utils.NUMBER_OF_DECIMALS, RoundingMode.HALF_UP),
                is(BigDecimal.ZERO));
    }

    @Test
    public void divideWithScaleByZeroTest() {
        Integer ten = Integer.valueOf(TEN);

        assertThat(BigDecimalUtils.divideWithScale(Integer.valueOf(ZERO), ten, Utils.NUMBER_OF_DECIMALS, RoundingMode
                        .HALF_UP),
                is(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.divideWithScale(null, ten, Utils.NUMBER_OF_DECIMALS, RoundingMode.HALF_UP),
                is(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.divideWithScale(Integer.valueOf(ZERO), null, Utils.NUMBER_OF_DECIMALS,
                RoundingMode.HALF_UP),
                is(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.divideWithScale(ten, Integer.valueOf(ZERO), Utils.NUMBER_OF_DECIMALS, RoundingMode
                        .HALF_UP),
                is(BigDecimal.ZERO));
    }

    @Test
    public void correctDevisionTestScaleAndMode() {
        BigDecimal value3 = new BigDecimal(3);
        BigDecimal correctRes = new BigDecimal(3.33);
        int scale = 2;
        RoundingMode mode = RoundingMode.HALF_UP;
        correctRes = correctRes.setScale(scale, mode);

        assertThat(BigDecimalUtils.divideAbyB(BigDecimal.TEN, value3, scale, mode), is(correctRes));
    }

    @Test
    public void devisionByZeroTestForIntegers() {
        BigDecimal value12 = new BigDecimal(12);

        assertThat(BigDecimalUtils.divideAsBigDecimals(0, 12), is(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.divideAsBigDecimals(null, 12), is(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.divideAsBigDecimals(0, null), is(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.divideAsBigDecimals(12, 0), is(BigDecimal.ZERO));

    }

    @Test
    public void devisionByZeroTestForLongs() {
        BigDecimal value12 = new BigDecimal(12);

        assertThat(BigDecimalUtils.divideAsBigDecimals(0L, 12L), is(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.divideAsBigDecimals(null, 12L), is(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.divideAsBigDecimals(0L, null), is(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.divideAsBigDecimals(12L, 0L), is(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.divideAsBigDecimals(12L, 3L), is(BigDecimal.valueOf(4L).setScale(2,
                RoundingMode.HALF_UP)));

    }

    @Test
    public void correctDevisionTestForIntegersAsBigDecimals() {
        assertThat(BigDecimalUtils.divideAsBigDecimals(9, 3), is(BigDecimal.valueOf(3L).setScale(2,
                RoundingMode.HALF_UP)));
        assertThat(BigDecimalUtils.divideAsBigDecimals(TEN, 4), is(BigDecimal.valueOf(2.5).setScale(2,
                RoundingMode.HALF_UP)));
    }

    @Test
    public void isNullableGreaterOrEqualsThan() {
        boolean isEquals = BigDecimalUtils.isNullableGreaterOrEqualsThan(new BigDecimal(11), null);
        assertFalse(isEquals);

        isEquals = BigDecimalUtils.isNullableGreaterOrEqualsThan(null, new BigDecimal(11));
        assertFalse(isEquals);

        isEquals = BigDecimalUtils.isNullableGreaterOrEqualsThan(new BigDecimal(11), new BigDecimal(11.1));
        assertFalse(isEquals);

        isEquals = BigDecimalUtils.isNullableGreaterOrEqualsThan(null, null);
        assertTrue(isEquals);

        isEquals = BigDecimalUtils.isNullableGreaterOrEqualsThan(new BigDecimal(11.1), new BigDecimal(11.1));
        assertTrue(isEquals);
    }

    @Test
    public void isNullOrEqualsZeroOrLessThanZero() {
        assertThat(BigDecimalUtils.isNullOrLessEqualZero(null), is(true));
        assertThat(BigDecimalUtils.isNullOrLessEqualZero(BigDecimal.ZERO), is(true));
        assertThat(BigDecimalUtils.isNullOrLessEqualZero(BigDecimal.ZERO.negate()), is(true));
        assertThat(BigDecimalUtils.isNullOrLessEqualZero(BigDecimal.TEN.negate()), is(true));
        assertThat(BigDecimalUtils.isNullOrLessEqualZero(BigDecimal.TEN), is(false));
    }

    private void assertSafeSum(int length, int start, int end, BigDecimal expected) {
        List<BigDecimal> list = new ArrayList<>(Collections.nCopies(length, new BigDecimal(1)));
        BigDecimal result = BigDecimalUtils.sumBigDecimalsSafe(list, start, end);
        assertThat(result, is(expected));
    }

    @Test
    public void isNullOrEqualsZero() {
        Number nullVal = null;
        assertThat(BigDecimalUtils.isNullOrEqualsZero(BigDecimal.valueOf(0.1)), is(false));
        assertThat(BigDecimalUtils.isNullOrEqualsZero(new BigDecimal("0.00")), is(true));
    }

    @Test
    public void isNotNullAndNotEqualsZero_worksForPositiveNumber() {
        assertThat(BigDecimalUtils.isNotNullAndNotEqualsZero(BigDecimal.ONE.negate()), is(true));
    }

    @Test
    public void isNotNullAndNotEqualsZero_worksForNegativeNumber() {
        assertThat(BigDecimalUtils.isNotNullAndNotEqualsZero(BigDecimal.ONE), is(true));
    }

    @Test
    public void isNotNullAndNotEqualsZero_worksForZero() {
        assertThat(BigDecimalUtils.isNotNullAndNotEqualsZero(BigDecimal.ZERO), is(false));
    }

    @Test
    public void isNotNullAndNotEqualsZero_worksForNull() {
        assertThat(BigDecimalUtils.isNotNullAndNotEqualsZero((BigDecimal) null), is(false));
    }

    @Test
    public void isBigDecimalNumberIsIntegerValue() {
        assertThat(isIntegerValue(BigDecimal.ZERO), is(true));
        assertThat(isIntegerValue(BigDecimal.ONE), is(true));
        assertThat(isIntegerValue(BigDecimal.valueOf(2.0)), is(true));
        assertThat(isIntegerValue(BigDecimal.valueOf(-2.0)), is(true));
        assertThat(isIntegerValue(BigDecimal.valueOf(-0.0)), is(true));
        assertThat(isIntegerValue(BigDecimal.valueOf(2.1)), is(false));
        assertThat(isIntegerValue(BigDecimal.valueOf(2.5)), is(false));
        assertThat(isIntegerValue(BigDecimal.valueOf(-2.9)), is(false));
        assertThat(isIntegerValue(BigDecimal.valueOf(2.0000000000000009)), is(false));
        assertThat(isIntegerValue(null), is(false));
    }

    @Test
    public void extractDigitsFromRightTest() {
        assertThat(extractDigitsFromRight(BigDecimal.ZERO, 1), is(0));
        assertThat(extractDigitsFromRight(null, 1), is(0));
        assertThat(extractDigitsFromRight(BigDecimal.valueOf(2525), 2), is(25));
    }

    @Test
    public void addTest() {
        assertThat(BigDecimalUtils.add(BigDecimal.ONE, BigDecimal.ONE), is(BigDecimal.valueOf(2)));
        assertThat(BigDecimalUtils.add(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE), is(BigDecimal.valueOf(3)));
        assertThat(BigDecimalUtils.add(null, BigDecimal.ONE, null), is(BigDecimal.ONE));
        assertThat(BigDecimalUtils.add(null, BigDecimal.ONE), is(BigDecimal.ONE));
        assertThat(BigDecimalUtils.add(BigDecimal.ONE, null), is(BigDecimal.ONE));
        assertThat(BigDecimalUtils.add(null, null), is(BigDecimal.ZERO));
    }

    @Test
    public void subtractTest() {
        assertThat(BigDecimalUtils.subtract(BigDecimal.ONE, BigDecimal.ONE), is(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.subtract(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE),
                is(BigDecimal.valueOf(-1)));
        assertThat(BigDecimalUtils.subtract(null, BigDecimal.ONE, null), is(BigDecimal.ONE));
        assertThat(BigDecimalUtils.subtract(null, null, null), is(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.subtract(null, BigDecimal.ONE), is(BigDecimal.ONE));
        assertThat(BigDecimalUtils.subtract(BigDecimal.ONE, null), is(BigDecimal.ONE));
        assertThat(BigDecimalUtils.subtract(null, null), is(BigDecimal.ZERO));


        assertThat(BigDecimalUtils.subtractInt(BigDecimal.ONE, Integer.valueOf(ONE)), is(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.subtractInt(BigDecimal.ONE, Integer.valueOf(TEN)),
                is(BigDecimal.valueOf(NEGATIVE_NINE)));
        assertThat(BigDecimalUtils.subtractInt(null, Integer.valueOf(ONE)), is(BigDecimal.ONE));
        assertThat(BigDecimalUtils.subtractInt(null, null), is(BigDecimal.ZERO));

        assertThat(BigDecimalUtils.subtractIntInt(Integer.valueOf(ONE), Integer.valueOf(ONE)), is(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.subtractIntInt(Integer.valueOf(ONE), Integer.valueOf(TEN)), is(BigDecimal.valueOf
                (NEGATIVE_NINE)));
        assertThat(BigDecimalUtils.subtractIntInt(null, Integer.valueOf(ONE)), is(BigDecimal.ONE));
        assertThat(BigDecimalUtils.subtractIntInt(null, null), is(BigDecimal.ZERO));
    }

    @Test
    public void multiplyTest() {
        assertThat(BigDecimalUtils.multiply(BigDecimal.ONE, BigDecimal.ONE), is(BigDecimal.ONE));
        assertThat(BigDecimalUtils.multiply(BigDecimal.ONE, BigDecimal.valueOf(2), BigDecimal.valueOf(3)),
                is(BigDecimal.valueOf(6)));
        assertThat(BigDecimalUtils.multiply(null, null, new MathContext(2, RoundingMode.HALF_UP)),
                is(new BigDecimal(0).round(new MathContext(2, RoundingMode.HALF_UP))));
    }

    @Test
    public void isLessOrEqualsThan() {
        assertThat(BigDecimalUtils.isLessOrEqualsThan(BigDecimal.ONE, BigDecimal.ONE), is(true));
        assertThat(BigDecimalUtils.isLessOrEqualsThan(BigDecimal.ZERO, BigDecimal.ONE), is(true));
        assertThat(BigDecimalUtils.isLessOrEqualsThan(BigDecimal.ONE, BigDecimal.ZERO), is(false));
        assertThat(BigDecimalUtils.isLessOrEqualsThan(null, BigDecimal.ZERO), is(false));
        assertThat(BigDecimalUtils.isLessOrEqualsThan(BigDecimal.ONE, null), is(false));
        assertThat(BigDecimalUtils.isLessOrEqualsThan(null, null), is(false));
    }

    @Test
    public void remainderTest() {
        assertThat(BigDecimalUtils.remainder(BigDecimal.ZERO, BigDecimal.ONE), is(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.remainder(BigDecimal.ONE, BigDecimal.ZERO), is(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.remainder(BIG_DEC_THIRTY, BIG_DEC_FOUR_POINT_SEVEN), is(BIG_DEC_ONE_POINT_EIGHT));
        assertThat(BigDecimalUtils.remainder(BIG_DEC_THREE, BIG_DEC_FOUR_POINT_SEVEN), is(BIG_DEC_THREE));
        assertThat(BigDecimalUtils.remainder(BigDecimal.ZERO, BIG_DEC_FOUR_POINT_SEVEN), is(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.remainder(BIG_DEC_FOUR_POINT_SEVEN, BigDecimal.ZERO), is(BigDecimal.ZERO));
    }

    @Test
    public void setScaleTest() {
        assertThat(BigDecimalUtils.setScale(BigDecimal.ZERO, 0, RoundingMode.FLOOR), is(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.setScale(BigDecimal.TEN, 1, RoundingMode.FLOOR), is(BigDecimal.TEN.setScale(1)));
    }
    @Test
    public void min() {
        assertThat(BigDecimalUtils.min(null, null), comparesEqualTo(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.min(null, BigDecimal.ZERO), comparesEqualTo(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.min(BigDecimal.ZERO, null), comparesEqualTo(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.min(BigDecimal.ZERO, new BigDecimal("2.50")), comparesEqualTo(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.min(new BigDecimal("3.20"), new BigDecimal("2.50")),
                comparesEqualTo(new BigDecimal("2.50")));
    }

    @Test
    public void max() {
        assertThat(BigDecimalUtils.max(null, null), comparesEqualTo(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.max(null, BigDecimal.ZERO), comparesEqualTo(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.max(BigDecimal.ZERO, null), comparesEqualTo(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.max(new BigDecimal("2.50"), null), comparesEqualTo(new BigDecimal("2.50")));
        assertThat(BigDecimalUtils.max(BigDecimal.ZERO, new BigDecimal("2.50")),
                comparesEqualTo(new BigDecimal("2.50")));
        assertThat(BigDecimalUtils.max(new BigDecimal("3.20"), new BigDecimal("2.50")),
                comparesEqualTo(new BigDecimal("3.20")));
    }

    @Test
    public void divideAbyBWIthMathContextTest() {
        int PRECISION_2 = 2;
        MathContext CONTEXT_PRECISION_2 = new MathContext(PRECISION_2, UP);
        assertThat(BigDecimalUtils.divideAbyB(null, BigDecimal.ONE, CONTEXT_PRECISION_2),
                comparesEqualTo(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.divideAbyB(BigDecimal.ONE, BigDecimal.ONE, CONTEXT_PRECISION_2),
                comparesEqualTo(BigDecimal.ONE));
        assertThat(BigDecimalUtils.divideAbyB(BigDecimal.valueOf(22), BigDecimal.valueOf(7), CONTEXT_PRECISION_2),
                comparesEqualTo(BigDecimal.valueOf(3.2)));
    }

    @Test
    public void multiply() {
        int PRECISION_2 = 2;
        MathContext CONTEXT_PRECISION_2 = new MathContext(PRECISION_2, UP);
        assertThat(BigDecimalUtils.multiply(null, BigDecimal.ONE, CONTEXT_PRECISION_2),
                comparesEqualTo(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.multiply(BigDecimal.ONE, null, CONTEXT_PRECISION_2),
                comparesEqualTo(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.multiply(null, null, CONTEXT_PRECISION_2),
                comparesEqualTo(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.multiply(BigDecimal.ONE, BigDecimal.ONE, CONTEXT_PRECISION_2),
                comparesEqualTo(BigDecimal.ONE));
        assertThat(BigDecimalUtils.multiply(BigDecimal.valueOf(2.2), BigDecimal.valueOf(7.1), CONTEXT_PRECISION_2),
                comparesEqualTo(BigDecimal.valueOf(16)));
    }

    @Test
    public void multiplyWithScaleAndRoundingModeTest() {
        assertThat(BigDecimalUtils.multiply(2, RoundingMode.HALF_UP, null, BigDecimal.ONE),
                comparesEqualTo(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.multiply(2, RoundingMode.HALF_UP, BigDecimal.ONE, null, null),
                comparesEqualTo(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.multiply(2, RoundingMode.HALF_UP, BigDecimal.valueOf(3.1454),
                BigDecimal.valueOf(7.12563)), comparesEqualTo(BigDecimal.valueOf(22.41)));
    }

    @Test
    public void divideAbyBDefaultRoundingModeTest() {
        assertThat(BigDecimalUtils.divideAbyBDefault(null, BigDecimal.ONE, RoundingMode.HALF_UP),
                comparesEqualTo(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.divideAbyBDefault(BigDecimal.ONE, BigDecimal.ONE, RoundingMode.HALF_UP),
                comparesEqualTo(BigDecimal.ONE));
        assertThat(BigDecimalUtils.divideAbyBDefault(BigDecimal.valueOf(13.35), BigDecimal.valueOf(3),
                RoundingMode.HALF_UP),
                comparesEqualTo(BigDecimal.valueOf(4.45)));
    }

    @Test
    public void remainderWithContextTest() {
        assertThat(BigDecimalUtils.remainderWithContext(null, BigDecimal.ONE, new MathContext(4,
                RoundingMode.HALF_UP)), comparesEqualTo(BigDecimal.ZERO));
        assertThat(BigDecimalUtils.remainderWithContext(new BigDecimal(3.54), new BigDecimal(4.547), new MathContext(4,
                RoundingMode.HALF_UP)), comparesEqualTo(new BigDecimal(3.54).remainder(new BigDecimal(4.547),
                new MathContext(4,
                        RoundingMode.HALF_UP))));
    }

    @Test
    public void trimMoreThan2LeftDigits(){
        assertThat(BigDecimalUtils.trimMoreThan2LeftDigits(
                new BigDecimal("12345.67")),
                is(new BigDecimal("45.67")));
    }

    @Test
    public void defaultIfNullOrZero() {
        assertThat(BigDecimalUtils.defaultIfNullOrZero(BigDecimal.ONE, null), is(BigDecimal.ONE));
        assertThat(BigDecimalUtils.defaultIfNullOrZero(null, BigDecimal.ONE), is(BigDecimal.ONE));
        assertThat(BigDecimalUtils.defaultIfNullOrZero(null, null), is(BigDecimal.ZERO));
    }

    @Test
    public void extractBigIntegerDigitsFromRight() {
        assertThat(BigDecimalUtils.extractBigIntegerDigitsFromRight(BigDecimal.ONE, 0), is(BigInteger.ZERO));
        assertThat(BigDecimalUtils.extractBigIntegerDigitsFromRight(BigDecimal.valueOf(123456), 4),
                is(BigInteger.valueOf(3456)));
    }

}
