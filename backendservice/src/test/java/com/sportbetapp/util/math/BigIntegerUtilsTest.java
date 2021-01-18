package com.sportbetapp.util.math;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.sportbetapp.util.math.BigIntegerUtils.isGreaterThanZero;
import static com.sportbetapp.util.math.BigIntegerUtils.toBigIntOrDefault;
import static com.sportbetapp.util.math.BigIntegerUtils.toIntOrDefault;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import org.junit.Test;

public class BigIntegerUtilsTest {

    @Test
    public void bigIntegerHelperMethods() {
        assertTrue(isGreaterThanZero(BigInteger.ONE));
        assertFalse(isGreaterThanZero(BigInteger.ZERO));
        assertFalse(isGreaterThanZero(BigInteger.ONE.negate()));
        assertFalse(isGreaterThanZero(null));
    }

    @Test
    public void toIntOrDefault_norm() {
        assertThat(toIntOrDefault(BigInteger.ZERO, null), is(0));
        assertThat(toIntOrDefault(BigInteger.valueOf(Integer.MAX_VALUE), null), is(Integer.MAX_VALUE));
        assertThat(toIntOrDefault(BigInteger.valueOf(Integer.MIN_VALUE), null), is(Integer.MIN_VALUE));
        assertThat(toIntOrDefault(null, 0), is(0));
        assertThat(toIntOrDefault(null, null), nullValue());
    }

    @Test
    public void toBigIntOrDefault_norm() {
        assertThat(toBigIntOrDefault(0, null), is(BigInteger.ZERO));
        assertThat(toBigIntOrDefault(Integer.MAX_VALUE, null), is(BigInteger.valueOf(Integer.MAX_VALUE)));
        assertThat(toBigIntOrDefault(Integer.MIN_VALUE, null), is(BigInteger.valueOf(Integer.MIN_VALUE)));
        assertThat(toBigIntOrDefault(null, BigInteger.ZERO), is(BigInteger.ZERO));
        assertThat(toBigIntOrDefault(null, null), nullValue());
    }

    @Test
    public void sanitizeSearchNumber() {
        assertThat(BigIntegerUtils.sanitizeSearchNumber(null), is(nullValue()));
        assertThat(BigIntegerUtils.sanitizeSearchNumber(BigInteger.ZERO), is(nullValue()));
        assertThat(BigIntegerUtils.sanitizeSearchNumber(BigInteger.valueOf(5)), is(BigInteger.valueOf(5)));
    }

    @Test
    public void bigIntegerDivide() {
        assertThat(BigIntegerUtils.divide(BigInteger.ZERO, null), is(BigDecimal.ZERO));
        assertThat(BigIntegerUtils.divide(null, null), is(BigDecimal.ZERO));
        assertThat(BigIntegerUtils.divide(null, 2), is(BigDecimal.ZERO));
        assertThat(BigIntegerUtils.divide(BigInteger.valueOf(4), 2), is(BigDecimal.valueOf(2).setScale(2,
                RoundingMode.HALF_UP)));
    }

    @Test
    public void bigIntegerDivideWithRoundingMode() {
        assertThat(BigIntegerUtils.divide(BigInteger.ZERO, null, RoundingMode.HALF_UP), is(BigInteger.ZERO));
        assertThat(BigIntegerUtils.divide(null, null, RoundingMode.HALF_UP), is(BigInteger.ZERO));
        assertThat(BigIntegerUtils.divide(null, 2, RoundingMode.HALF_UP), is(BigInteger.ZERO));
        assertThat(BigIntegerUtils.divide(BigInteger.valueOf(4), 2, RoundingMode.HALF_UP),
                is(BigInteger.valueOf(2)));
    }

    @Test
    public void isZeroOrNull() {
        assertThat(BigIntegerUtils.isZeroOrNull(null), is(true));
        assertThat(BigIntegerUtils.isZeroOrNull(BigInteger.ZERO), is(true));
        assertThat(BigIntegerUtils.isZeroOrNull(BigInteger.ONE), is(false));
    }

    @Test
    public void addTest() {
        assertThat(BigIntegerUtils.add(BigInteger.ONE, BigInteger.ONE), is(BigInteger.valueOf(2)));
        assertThat(BigIntegerUtils.add(BigInteger.ONE, BigInteger.ONE, BigInteger.ONE), is(BigInteger.valueOf(3)));
        assertThat(BigIntegerUtils.add(null, BigInteger.ONE, null), is(BigInteger.ONE));
    }

    @Test
    public void subtractTest() {
        assertThat(BigIntegerUtils.subtract(BigInteger.ONE, BigInteger.ONE), is(BigInteger.ZERO));
        assertThat(BigIntegerUtils.subtract(BigInteger.ONE, BigInteger.ONE, BigInteger.ONE),
                is(BigInteger.valueOf(-1)));
        assertThat(BigIntegerUtils.subtract(null, BigInteger.ONE, null), is(BigInteger.ONE));
        assertThat(BigIntegerUtils.subtract(null, null, null), is(BigInteger.ZERO));
    }

    @Test
    public void multiplyTest() {
        assertThat(BigIntegerUtils.multiply(BigInteger.ONE, BigInteger.ONE), is(BigInteger.ONE));
        assertThat(BigIntegerUtils.multiply(BigInteger.ONE, BigInteger.valueOf(2), BigInteger.valueOf(3)),
                is(BigInteger.valueOf(6)));
        assertThat(BigIntegerUtils.multiply(null, BigInteger.ONE, null), is(BigInteger.ZERO));
        assertThat(BigIntegerUtils.multiply(null, null, null), is(BigInteger.ZERO));
        assertThat(BigIntegerUtils.multiply(BigInteger.ZERO, BigInteger.ONE, null), is(BigInteger.ZERO));
    }

    @Test
    public void equalTest() {
        assertThat(BigIntegerUtils.equals(BigInteger.ONE, BigInteger.ONE), is(true));
        assertThat(BigIntegerUtils.equals(BigInteger.ONE, BigInteger.TEN), is(false));
        assertThat(BigIntegerUtils.equals(BigInteger.ZERO, BigInteger.ZERO), is(true));
        assertThat(BigIntegerUtils.equals(BigInteger.ZERO, null), is(false));
        assertThat(BigIntegerUtils.equals(null, BigInteger.ONE), is(false));
        assertThat(BigIntegerUtils.equals(null, null), is(false));
    }

    @Test
    public void isGreaterThanOrEquals() {
        boolean isEquals = BigIntegerUtils.isGreaterThanOrEquals(null, null);
        assertFalse(isEquals);

        isEquals = BigIntegerUtils.isGreaterThanOrEquals(null, BigInteger.valueOf(11));
        assertFalse(isEquals);

        isEquals = BigIntegerUtils.isGreaterThanOrEquals(BigInteger.valueOf(12), BigInteger.valueOf(11));
        assertTrue(isEquals);

        isEquals = BigIntegerUtils.isGreaterThanOrEquals(BigInteger.valueOf(11), BigInteger.valueOf(11));
        assertTrue(isEquals);
    }

    @Test
    public void isLessThanOrEquals() {
        boolean isEquals = BigIntegerUtils.isLessThanOrEquals(null, null);
        assertFalse(isEquals);

        isEquals = BigIntegerUtils.isLessThanOrEquals(null, BigInteger.valueOf(11));
        assertFalse(isEquals);

        isEquals = BigIntegerUtils.isLessThanOrEquals(BigInteger.valueOf(10), BigInteger.valueOf(11));
        assertTrue(isEquals);

        isEquals = BigIntegerUtils.isLessThanOrEquals(BigInteger.valueOf(11), BigInteger.valueOf(11));
        assertTrue(isEquals);
    }

    @Test
    public void max() {
        assertThat(BigIntegerUtils.max(null, null), comparesEqualTo(BigInteger.ZERO));
        assertThat(BigIntegerUtils.max(null, BigInteger.ZERO), comparesEqualTo(BigInteger.ZERO));
        assertThat(BigIntegerUtils.max(BigInteger.ZERO, null), comparesEqualTo(BigInteger.ZERO));
        assertThat(BigIntegerUtils.max(new BigInteger("2"), null), comparesEqualTo(new BigInteger("2")));
        assertThat(BigIntegerUtils.max(BigInteger.ZERO, new BigInteger("2")),
                comparesEqualTo(new BigInteger("2")));
        assertThat(BigIntegerUtils.max(new BigInteger("3"), new BigInteger("2")),
                comparesEqualTo(new BigInteger("3")));
    }

    @Test
    public void toBigIntegerOrZeroTest() {
        assertThat(BigIntegerUtils.toBigIntegerOrZero(null), is(BigInteger.ZERO));
        assertThat(BigIntegerUtils.toBigIntegerOrZero(BigInteger.TEN), is(BigInteger.TEN));
    }
}
