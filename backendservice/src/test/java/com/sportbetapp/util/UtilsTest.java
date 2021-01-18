package com.sportbetapp.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static com.sportbetapp.util.Utils.isEqualsZero;
import static com.sportbetapp.util.Utils.isGreaterThanZero;
import static com.sportbetapp.util.Utils.isLessOrEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

public class UtilsTest {

    private final static String ZERO = "0";

    @Test
    public void allEquals() {
        String[] vars = {"A", "A", "A"};
        assertTrue(Utils.allEquals("A", vars));

        vars[2] = "B";
        assertFalse(Utils.allEquals("A", vars));

        vars = new String[0];
        assertFalse(Utils.allEquals("A", vars));

        vars = null;
        assertFalse(Utils.allEquals("A", vars));
    }

    @Test
    public void containsAll() {
        ImmutableSet<Integer> set = ImmutableSet.of(1, 2, 3, 4);
        Integer[] vars = {4, 2,3};
        assertTrue(Utils.containsAll(set, vars));

        vars[2] = 99;
        assertFalse(Utils.containsAll(set, vars));

        vars = new Integer[0];
        assertFalse(Utils.containsAll(set, vars));

        vars = null;
        assertFalse(Utils.containsAll(set, vars));

    }

    @Test
    public void isBetweenOrEq() {
        assertThat(Utils.isBetweenOrEq(1, 1, 1), is(true));
        assertThat(Utils.isBetweenOrEq(1, 1, 2), is(true));
        assertThat(Utils.isBetweenOrEq(1, 2, 2), is(true));
        assertThat(Utils.isBetweenOrEq(1, 0, 2), is(false));
        assertThat(Utils.isBetweenOrEq(1, 3, 2), is(false));

        LocalDate date = LocalDate.of(1999, 9, 9);
        LocalDate before = date.minusDays(1);
        LocalDate after = date.plusDays(1);
        assertThat(Utils.isBetweenOrEq(date, date, date), is(true));
        assertThat(Utils.isBetweenOrEq(date, date, after), is(true));
        assertThat(Utils.isBetweenOrEq(date, after, after), is(true));
        assertThat(Utils.isBetweenOrEq(date, before, after), is(false));
        assertThat(Utils.isBetweenOrEq(date, after.plusDays(2), after), is(false));
    }

    @Test
    public void bulkAnd_normal() {
        assertThat(Utils.bulkAnd(true, true), is(true));
        assertThat(Utils.bulkAnd(false, true), is(false));
    }

    @Test
    public void bulkAnd_nullArg() {
        assertThat(Utils.bulkAnd(null, null), is(false));
        assertThat(Utils.bulkAnd(null, true), is(false));
    }

    @Test
    public void bulkAnd_emptyArg() {
        Boolean[] arg = {};
        assertThat(Utils.bulkAnd(arg), is(false));
    }

    @Test
    public void bulkOr_normal() {
        assertThat(Utils.bulkOr(true, true), is(true));
        assertThat(Utils.bulkOr(false, true), is(true));
        assertThat(Utils.bulkOr(false, false), is(false));
    }

    @Test
    public void bulkOr_nullArg() {
        assertThat(Utils.bulkOr(null, null), is(false));
        assertThat(Utils.bulkOr(null, false), is(false));
        assertThat(Utils.bulkOr(null, true), is(true));
    }

    @Test
    public void bulkOr_emptyArg() {
        Boolean[] arg = {};
        assertThat(Utils.bulkOr(arg), is(false));
    }

    @Test
    public void getOrDefault_returnDefaultValue() {
        assertThat(Utils.getOrDefault(null, 0), is(0));
    }

    @Test
    public void getOrDefault_returnValue() {
        assertThat(Utils.getOrDefault(1, 0), is(1));
    }

    @Test
    public void isBeforeOrEq_returnFalse() {
        LocalDate date = LocalDate.of(1999,9,9);
        assertFalse(Utils.isBeforeOrEq(date.plusDays(1), date));
    }

    @Test
    public void isBeforeOrEq_returnTrue() {
        LocalDate date = LocalDate.of(1999,9,9);
        assertTrue(Utils.isBeforeOrEq(date, date));
        assertTrue(Utils.isBeforeOrEq(date.minusDays(1), date));
    }

    @Test
    public void isBefore() {
        LocalDate dateBefore = LocalDate.of(1999,9,9);
        LocalDate dateAfter = LocalDate.of(2000,1,1);

        assertFalse(Utils.isBefore(null, null));
        assertFalse(Utils.isBefore(dateBefore, null));
        assertFalse(Utils.isBefore(null, dateBefore));

        assertTrue(Utils.isBefore(dateBefore, dateAfter));
        assertFalse(Utils.isBefore(dateAfter, dateBefore));
        assertFalse(Utils.isBefore(dateBefore, dateBefore));
    }

    @Test
    public void isAfterOrEq_returnFalse() {
        LocalDate date = LocalDate.of(1999,9,9);
        assertFalse(Utils.isAfterOrEq(date.minusDays(1), date));
    }

    @Test
    public void isAfterOrEq_returnTrue() {
        LocalDate date = LocalDate.of(1999,9,9);
        assertTrue(Utils.isAfterOrEq(date, date));
        assertTrue(Utils.isAfterOrEq(date.plusDays(1), date));
    }

    @Test
    public void isAfter_returnFalse() {
        LocalDate date = LocalDate.of(1999,9,9);
        assertFalse(Utils.isAfter(date, date));
        assertFalse(Utils.isAfter(date.minusDays(1), date));
    }

    @Test
    public void isAfter_returnTrue() {
        LocalDate date = LocalDate.of(1999,9,9);
        assertTrue(Utils.isAfter(date.plusDays(1), date));
    }

    @Test
    public void formatCode() {
        assertThat(Utils.formatCode(null), is(nullValue()));
        assertThat(Utils.formatCode(""), isEmptyString());
        assertThat(Utils.formatCode("1"), is("1"));
        assertThat(Utils.formatCode("1234"), is("1234"));
        assertThat(Utils.formatCode("1-2345"), is("12345"));
        assertThat(Utils.formatCode("12-34567"), is("1234567"));
    }

    @Test
    public void parseProductNumber() {
        assertThat(Utils.parseProductNumber("1"), is(1L));
        assertThat(Utils.parseProductNumber("1234"), is(1234L));
        assertThat(Utils.parseProductNumber("1-2345"), is(12345L));
        assertThat(Utils.parseProductNumber("12-34567"), is(1234567L));
    }

    @Test
    public void formatProductNumber() {
        assertThat(Utils.formatProductNumber(1), is("1"));
        assertThat(Utils.formatProductNumber(1234), is("1234"));
        assertThat(Utils.formatProductNumber(12345), is("1-2345"));
        assertThat(Utils.formatProductNumber(1234567), is("123-4567"));
    }

    @Test
    public void formatNumberWithDelimiter() {
        assertThat(Utils.formatNumberWithDelimiter(1, 2), is("1"));
        assertThat(Utils.formatNumberWithDelimiter(1234, 2), is("12-34"));
        assertThat(Utils.formatNumberWithDelimiter(1234, 5), is("1234"));
        assertThat(Utils.formatNumberWithDelimiter(1234, 3), is("1-234"));
        assertThat(Utils.formatNumberWithDelimiter(1234567, 5), is("12-34567"));
    }

    @Test
    public void camelCaseToUnderscore() {
        assertThat(Utils.camelCaseToUnderscore("offeringId"), is("Offering_Id"));
        assertThat(Utils.camelCaseToUnderscore("priceSeqNumber"), is("Price_Seq_Number"));
        assertThat(Utils.camelCaseToUnderscore("changeDTSDatetime"), is("Change_DTS_Datetime"));
    }

    @Test
    public void isNotEqualsForComparableObjects() {
        assertTrue(Utils.isNotEquals(null, null));
        assertTrue(Utils.isNotEquals(null, BigDecimal.ZERO));
        assertTrue(Utils.isNotEquals(BigDecimal.ZERO, null));
        assertTrue(Utils.isNotEquals(new BigDecimal("1.0"), new BigDecimal("5.0")));

        assertFalse(Utils.isNotEquals(BigDecimal.ZERO, BigDecimal.ZERO));
        assertFalse(Utils.isNotEquals(new BigDecimal("0.00000"), new BigDecimal("0.00000")));
        assertFalse(Utils.isNotEquals(BigDecimal.ZERO, new BigDecimal("0.00000")));
        assertFalse(Utils.isNotEquals(new BigDecimal("10"), new BigDecimal("10.00")));
    }

    @Test
    public void bigDecimalHelperMethods() {
        assertTrue(isLessOrEquals(BigDecimal.ONE, BigDecimal.TEN));
        assertTrue(isLessOrEquals(BigDecimal.ONE, BigDecimal.ONE));
        assertFalse(isLessOrEquals(BigDecimal.TEN, BigDecimal.ONE));
        assertFalse(isLessOrEquals(null, BigDecimal.TEN));
        assertFalse(isLessOrEquals(BigDecimal.TEN, null));

        assertTrue(isGreaterThanZero(1));
        assertFalse(isGreaterThanZero(0));

        assertTrue(isEqualsZero(0L));
    }

    @Test
    public void isZero() {
        assertThat(Utils.isZero(0), is(true));
        assertThat(Utils.isZero(0L), is(true));
        assertThat(Utils.isZero(0.0), is(true));
        assertThat(Utils.isZero(0.000000000000000000000001), is(true));
        assertThat(Utils.isZero(5D), is(false));
    }

    @Test
    public void isZero_threshold() {
        assertThat(Utils.isZero(0.0, 100), is(true));
        assertThat(Utils.isZero(0.5, 100), is(true));
        assertThat(Utils.isZero(5000D, 100), is(false));
    }

    @Test
    public void isNonZero() {
        assertThat(Utils.isNonZero((Long) null), is(false));
        assertThat(Utils.isNonZero(0L), is(false));
        assertThat(Utils.isNonZero(10L), is(true));
    }

    @Test
    public void isAnyOf() {
        String value = "some value";
        assertThat(Utils.isAnyOf(value), is(false));
        assertThat(Utils.isAnyOf(value, null), is(false));
        assertThat(Utils.isAnyOf(value, "1", "2"), is(false));
        assertThat(Utils.isAnyOf(value, "1", "2", value), is(true));
    }

    @Test
    public void isNullOrEqualsZero() {
        Number nullVal = null;
        assertThat(Utils.isNullOrEqualsZero(nullVal), is(true));
        assertThat(Utils.isNullOrEqualsZero((Number)0), is(true));
        assertThat(Utils.isNullOrEqualsZero(0.0), is(true));
        assertThat(Utils.isNullOrEqualsZero(5), is(false));
        assertThat(Utils.isNullOrEqualsZero(0.1), is(false));
        assertThat(Utils.isNullOrEqualsZero(0.1f), is(false));
    }

    @Test
    public void isNullOrEqualsZero_long() {
        Long nullVal = null;
        assertThat(Utils.isNullOrEqualsZero(nullVal), is(true));
        assertThat(Utils.isNullOrEqualsZero(0L), is(true));
        assertThat(Utils.isNullOrEqualsZero(5L), is(false));
        assertThat(Utils.isNullOrEqualsZero(-5L), is(false));
    }

    @Test
    public void isNullOrEqualsZero_date() {
        assertTrue(Utils.isNullOrEqualsZero((LocalDate) null));
        assertTrue(Utils.isNullOrEqualsZero(LocalDate.of(1900, 1, 1)));

        assertFalse(Utils.isNullOrEqualsZero(LocalDate.of(2019, 1, 1)));
    }

    @Test
    public void isNotNullAndNotEqualsZero_date() {
        assertFalse(Utils.isNotNullAndNotEqualsZero((LocalDate) null));
        assertFalse(Utils.isNotNullAndNotEqualsZero(LocalDate.of(1900, 1, 1)));

        assertTrue(Utils.isNotNullAndNotEqualsZero(LocalDate.of(2019, 1, 1)));
    }

    @Test
    public void isAnyNull_notHaveNulls() {

        assertThat("Empty array should not have nulls", Utils.isAnyNull(), is(false));
        assertThat("Empty array should not have nulls", Utils.isAnyNull(new Object()), is(false));
        assertThat("Empty array should not have nulls", Utils.isAnyNull(new Object(), new Object()), is(false));

    }

    @Test
    public void isAnyNull_haveNulls() {
        assertThat("Should contain nulls", Utils.isAnyNull(null), is(true));
        assertThat("Should contain nulls", Utils.isAnyNull(new Object(), null, new Object()), is(true));
    }


    @Test
    public void isStrictOne_notTrowExceptionIfListIsEmpty() {
        Utils.assertIsNoMoreOneElement(Lists.newArrayList());
    }

    @Test
    public void round_places2() {
        assertThat(12.35, is(Utils.round(12.3456789, 2)));
    }

    @Test
    public void round_placesZero() {
        assertThat(12.0, is(Utils.round(12.3456789, 0)));
    }

    @Test
    public void arrayRoundToInt() {
        BigDecimal[] array = new BigDecimal[] { new BigDecimal(ZERO), new BigDecimal("-1.5"), new BigDecimal("1.49"),
                new BigDecimal("1.50"), new BigDecimal("1.51"), };
        int[] expected = new int[] { 0, -2, 1, 2, 2 };
        int[] actual = Utils.arrayRoundToInt(array);
        assertThat(actual.length, is(expected.length));
        for (int i = 0; i < actual.length; i++) {
            assertThat(actual[i], is(expected[i]));
        }
    }

    @Test
    public void sanitizeSearchNumber() {
        assertThat(Utils.sanitizeSearchNumber((Integer) null), is(nullValue()));
        assertThat(Utils.sanitizeSearchNumber(0), is(nullValue()));
        assertThat(Utils.sanitizeSearchNumber(5), is(5));
        assertThat(Utils.sanitizeSearchNumber((Long) null), is(nullValue()));
        assertThat(Utils.sanitizeSearchNumber(0L), is(nullValue()));
        assertThat(Utils.sanitizeSearchNumber(5L), is(5L));
    }

    @Test
    public void givenNullIntegerWhenCallingIsGreaterThanOneThenUtilsShouldReturnFalse() {
        assertThat(Utils.isGreaterThanOne(null), is(false));
    }

    @Test
    public void givenIntegerLessThanOneWhenCallingIsGreaterThanOneThenUtilsShouldReturnFalse() {
        assertThat(Utils.isGreaterThanOne(0), is(false));
    }

    @Test
    public void givenIntegerEqualToOneWhenCallingIsGreaterThanOneThenUtilsShouldReturnFalse() {
        assertThat(Utils.isGreaterThanOne(1), is(false));
    }

    @Test
    public void givenIntegerGreaterThanOneWhenCallingIsGreaterThanOneThenUtilsShouldReturnTrue() {
        assertThat(Utils.isGreaterThanOne(2), is(true));
    }

    @Test
    public void bothZeroOrEquals() {
        assertThat(Utils.bothZeroOrEquals(null, null), is(true));
        assertThat(Utils.bothZeroOrEquals(null, 0), is(true));
        assertThat(Utils.bothZeroOrEquals(0, null), is(true));
        assertThat(Utils.bothZeroOrEquals(0, 0), is(true));

        assertThat(Utils.bothZeroOrEquals(null, 10), is(false));
        assertThat(Utils.bothZeroOrEquals(0, 10), is(false));
        assertThat(Utils.bothZeroOrEquals(10, null), is(false));
        assertThat(Utils.bothZeroOrEquals(10, 0), is(false));
        assertThat(Utils.bothZeroOrEquals(10, 20), is(false));

        assertThat(Utils.bothZeroOrEquals(10, 10), is(true));
    }

    @Test
    public void newLineCharactersEscapeSameNullValueResult() {
        assertThat(Utils.escapeCrlfSequence(null), Matchers.equalTo("null"));
    }

    @Test
    public void newLineCharactersEscapeSameEmptyValueResult() {
        assertThat(Utils.escapeCrlfSequence(""), Matchers.equalTo(""));
    }

    @Test
    public void newLineCharactersEscapeEscapedResult() {
        assertThat(Utils.escapeCrlfSequence("User was \r authenticated successfully\r\n[INFO] User bbb \n"),
                Matchers.equalTo("User was  authenticated successfully[INFO] User bbb "));
    }

    @Test
    public void sort() {
        List<Integer> original = new ArrayList<>();
        original.add(1);
        original.add(3);
        original.add(2);

        List<Integer> sorted = Utils.sort(original, Comparator.naturalOrder());
        assertThat(sorted, is(ImmutableList.of(1, 2, 3)));

        original.add(4);
        // ensure that sort method do not return same instance
        assertThat(sorted, is(ImmutableList.of(1, 2, 3)));
    }

    @Test
    public void toStringOrDefault() {
        assertThat(Utils.toStringOrDefault(null, "default"), Is.is("default"));
        assertThat(Utils.toStringOrDefault("value", "default"), Is.is("value"));
        assertThat(Utils.toStringOrDefault(null, null), Is.is(""));
    }

    @Test
    public void concat() {
        assertThat(Utils.concat(ImmutableList.of(), ImmutableList.of()), is(ImmutableList.of()));
        assertThat(Utils.concat(ImmutableList.of(1, 1), ImmutableList.of(1)), is(ImmutableList.of(1, 1, 1)));
        assertThat(Utils.concat(ImmutableList.of(1, 2), ImmutableList.of(3, 4)), is(ImmutableList.of(1, 2, 3, 4)));
        assertThat(Utils.concat(ImmutableList.of(1, 2), ImmutableSet.of(1)), is(ImmutableList.of(1, 2, 1)));
    }

    @Test
    public void substringFromLeft() {
        assertThat(Utils.trimFromLeft(123456, 4), is(1234));
        assertThat(Utils.trimFromLeft(BigDecimal.valueOf(123456), 4), is(BigDecimal.valueOf(1234)));
        assertThat(Utils.trimFromLeft(123456L, 4), is(1234L));
        assertThat(Utils.trimFromLeft(BigInteger.valueOf(123456), 4), is(BigInteger.valueOf(1234)));

        assertThat(Utils.trimFromLeft(123456, 7), is(123456));
        assertThat(Utils.trimFromLeft(BigDecimal.valueOf(123456), 7), is(BigDecimal.valueOf(123456)));
        assertThat(Utils.trimFromLeft(123456L, 7), is(123456L));
        assertThat(Utils.trimFromLeft(BigInteger.valueOf(123456), 7), is(BigInteger.valueOf(123456)));

        String nullStr = null;
        BigInteger nullBInt = null;
        BigDecimal nullBDec = null;
        Long nullLong = null;

        assertNull(Utils.trimFromLeft(nullStr, 7));
        assertNull(Utils.trimFromLeft(nullBDec, 7));
        assertNull(Utils.trimFromLeft(nullLong, 7));
        assertNull(Utils.trimFromLeft(nullBInt, 7));
    }

    @Test
    public void nonNullOrNonZeroToString() {
        assertThat(Utils.nonNullOrNonZeroToString(10, ""), is("10"));
        assertThat(Utils.nonNullOrNonZeroToString(0, ""), is(""));
        assertThat(Utils.nonNullOrNonZeroToString(BigDecimal.valueOf(10), ""), is("10"));
        assertThat(Utils.nonNullOrNonZeroToString(BigDecimal.ZERO, ""), is(""));
        assertThat(Utils.nonNullOrNonZeroToString(BigInteger.valueOf(10), ""), is("10"));
        assertThat(Utils.nonNullOrNonZeroToString(BigInteger.ZERO, ""), is(""));
        assertThat(Utils.nonNullOrNonZeroToString(10L, ""), is("10"));
        assertThat(Utils.nonNullOrNonZeroToString(0L, ""), is(""));
        assertThat(Utils.nonNullOrNonZeroToString(10d, ""), is("10.0"));
        assertThat(Utils.nonNullOrNonZeroToString(0d, ""), is(""));

        assertThat(Utils.nonNullOrNonZeroToString(null, ""), is(""));
    }

    @Test
    public void substringFromLeftText() {
        assertThat(Utils.trimFromLeft("Some value", 6), is("Some v"));
        assertThat(Utils.trimFromLeft("Some", 6), is("Some"));
        assertThat(Utils.trimFromLeft("", 6), is(""));
    }

    @Test
    public void bothBlankOrEquals() {
        assertTrue(Utils.bothBlankOrEquals(null, null));
        assertTrue(Utils.bothBlankOrEquals(null, ""));
        assertTrue(Utils.bothBlankOrEquals("", null));
        assertTrue(Utils.bothBlankOrEquals("", ""));
        assertTrue(Utils.bothBlankOrEquals(" ", null));
        assertTrue(Utils.bothBlankOrEquals(null, " "));
        assertTrue(Utils.bothBlankOrEquals("  ", " "));
        assertTrue(Utils.bothBlankOrEquals("A", "A"));
        assertFalse(Utils.bothBlankOrEquals("A", null));
        assertFalse(Utils.bothBlankOrEquals(null, "A"));
        assertFalse(Utils.bothBlankOrEquals(" ", "A"));
        assertFalse(Utils.bothBlankOrEquals("A", "B"));
        assertFalse(Utils.bothBlankOrEquals(" A", "A"));
    }

    @Test
    public void isLessOrEqualsThan() {
        assertFalse(Utils.isLessOrEqualsThan(null, null));
        assertFalse(Utils.isLessOrEqualsThan(0, null));
        assertFalse(Utils.isLessOrEqualsThan(null, 0));
        assertTrue(Utils.isLessOrEqualsThan(0, 0));
        assertTrue(Utils.isLessOrEqualsThan(1, 5));
        assertTrue(Utils.isLessOrEqualsThan(-1, 5));
        assertFalse(Utils.isLessOrEqualsThan(5, 1));
        assertFalse(Utils.isLessOrEqualsThan(5, -1));
    }

    @Test
    public void replaceNonDigitSymbolsToZero(){
        assertThat(Utils.replaceNonDigitSymbolsToZero("123"), is("123"));
        assertThat(Utils.replaceNonDigitSymbolsToZero("a23"), is("023"));
        assertThat(Utils.replaceNonDigitSymbolsToZero("12b"), is("120"));
        assertThat(Utils.replaceNonDigitSymbolsToZero("qqq"), is("000"));
        assertThat(Utils.replaceNonDigitSymbolsToZero(" "), is("0"));
        assertThat(Utils.replaceNonDigitSymbolsToZero(" s 123  "), is("00012300"));
        assertThat(Utils.replaceNonDigitSymbolsToZero("!@#$%^&*()_+"), is("000000000000"));
        assertThat(Utils.replaceNonDigitSymbolsToZero(""), is(""));
        assertThat(Utils.replaceNonDigitSymbolsToZero(null), nullValue());
    }

}
