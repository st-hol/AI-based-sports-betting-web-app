package com.sportbetapp.util;



import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Iterables;
import com.google.common.math.DoubleMath;
import com.sportbetapp.util.math.BigDecimalUtils;
import com.sportbetapp.util.math.NumberUtils;

@SuppressWarnings({"squid:S109", "frontier-java:AvoidToString"})
public final class Utils {
    public static final LocalDate ZERO_DATE = LocalDate.of(1900, 1, 1);
    public static final LocalTime ZERO_TIME = LocalTime.MIN;
    public static final LocalDateTime ZERO_DATE_TIME = LocalDateTime.of(ZERO_DATE, ZERO_TIME);
    public static final LocalDate MAX_DATE = LocalDate.of(9999, 12, 31);
    public static final Integer MIN_YEAR = ZERO_DATE.getYear();

    public static final int DECIMAL_4_MAX = 9999;
    public static final int DECIMAL_4_MIN = -9999;
    public static final int DECIMAL_5_MAX = 99999;
    public static final int DECIMAL_5_MIN = -99999;
    public static final int DECIMAL_7_MAX = 9999999;
    public static final int DECIMAL_7_MIN = -9999999;
    public static final String ZERO = "0";
    public static final String ON_OBJECT = " on object ";
    private static final String EMPTY_STRING = "";
    private static final double DOUBLE_THRESHOLD = 0.000001;
    public static final int NUMBER_OF_DECIMALS = 2;
    public static final int PRODUCT_SCALE = 10000;
    public static final int ZERO_INT = 0;
    private static final long ZERO_LONG = 0L;
    public static final String DELIMITER = "-";
    private static final int HUNDRED = 100;
    private static final String ZERO_STRING_PATTERN = "^0+$";
    private static final String OBJECT_NOT_VALID = "%s object state is not valid.";

    private Utils() {
        super();
    }

    @SafeVarargs
    public static <T extends Comparable<T>> boolean allEquals(T value, T... vars) {
        if (vars == null || vars.length == 0) {
            return false;
        }
        for (T var : vars) {
            if (!Objects.equals(value, var)) {
                return false;
            }
        }
        return true;
    }

    @SafeVarargs
    public static <T extends Comparable<T>> boolean containsAll(Set<T> valuesSet, T... vars) {
        if (valuesSet == null || valuesSet.isEmpty() || vars == null || vars.length == 0) {
            return false;
        }
        for (T var : vars) {
            if (!valuesSet.contains(var)) {
                return false;
            }
        }
        return true;
    }

    public static int cutIntToDecimal5(int value) {
        int maxValue = DECIMAL_5_MAX + 1;
        return value - (value / maxValue) * maxValue;
    }


    public static Integer cropIntToDecimal5(Integer value) {
        return cropValue(value, DECIMAL_5_MIN, DECIMAL_5_MAX);
    }

    public static Long cropLongToDecimal5(Long value) {
        return cropValue(value, (long) DECIMAL_5_MIN, (long) DECIMAL_5_MAX);
    }

    public static Integer cropIntToDecimal7(Integer value) {
        return cropValue(value, DECIMAL_7_MIN, DECIMAL_7_MAX);
    }

    public static Integer cropIntToDecimal4(Integer value) {
        return cropValue(value, DECIMAL_4_MIN, DECIMAL_4_MAX);
    }

    public static <T extends Comparable<T>> void cropField(Supplier<T> getter, Consumer<T> setter, T minValue, T maxValue) {
        setter.accept(cropValue(getter.get(), minValue, maxValue));
    }

    public static <T extends Comparable<T>> T cropValue(T value, T minValue, T maxValue) {
        if (value == null) {
            return null;
        }

        T result = value;

        if (maxValue != null && value.compareTo(maxValue) > 0) {
            result = maxValue;
        } else if (minValue != null && value.compareTo(minValue) < 0) {
            result = minValue;
        }

        return result;
    }

    public static boolean isZeroOrNull(Number number) {
        return number == null || number.intValue() == 0;
    }

    public static String camelCaseToUnderscore(String camelCase) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < camelCase.length(); i++) {
            char charI = camelCase.charAt(i);
            if (isItAfterUnderscore(i, camelCase)) {
                result.append("_");
            }
            if (isStartWord(i, charI, camelCase)) {
                if (i != 0) {
                    result.append("_");
                }
                result.append(Character.toUpperCase(charI));
            } else {
                result.append(charI);
            }
        }
        return result.toString();
    }

    private static boolean isItAfterUnderscore(int i, String camelCase) {
        return i > 0 && camelCase.charAt(i - 1) == '_';
    }

    private static boolean isStartWord(int i, char charI, String camelCase) {
        if (i == 0) {
            return true;
        }
        if (i == camelCase.length() - 1) {
            return false;
        }
        if (!Character.isUpperCase(charI)) {
            return false;
        }
        return !Character.isUpperCase(camelCase.charAt(i - 1)) || !Character.isUpperCase(camelCase.charAt(i + 1));
    }

    public static String formatCode(String codeString) {
        return codeString == null ? null : codeString.replaceAll("-", "");
    }

    public static String replaceSpaceToZero(String value) {
        return value == null ? null : value.replaceAll("\\s", ZERO);
    }

    public static Long parseProductNumber(String codeString) {
        return Long.parseLong(formatCode(codeString));
    }

    public static String formatProductNumber(long productNumber) {
        // 12345 => 1-2345
        return formatNumberWithDelimiter(productNumber, 4);
    }

    public static String formatOfferingNumber(long offeringNumber, Integer productCheckDigitNumber) {
        long prodClass = offeringNumber / PRODUCT_SCALE;
        long productNumber = offeringNumber - prodClass * PRODUCT_SCALE;
        return String.format("%3d-%04d-%1s", prodClass, productNumber, Objects.toString(productCheckDigitNumber, ""));
    }


    public static String formatOfferingNumberWithZeroes(long offeringNumber) {
        long prodClass = offeringNumber / PRODUCT_SCALE;
        long productNumber = offeringNumber - prodClass * PRODUCT_SCALE;
        return String.format("%01d-%04d", prodClass, productNumber);
    }

    public static Integer getClassCodeNumberFromOfferingNumber(Long offeringNumber) {
        return offeringNumber == null ? null : (int) ((NumberUtils.unbox(offeringNumber) / PRODUCT_SCALE) % HUNDRED);
    }

    public static String formatNumberWithDelimiter(long number, int afterDelimiterLength) {
        String raw = Long.toString(number);
        int delimiterInd = raw.length() - afterDelimiterLength;
        return (raw.length() > afterDelimiterLength)
                ? (raw.substring(0, delimiterInd) + DELIMITER + raw.substring(delimiterInd)) : raw;
    }

    public static boolean isZero(Integer value) {
        return value != null && value == 0;
    }

    public static boolean isZero(Long value) {
        return value != null && value == 0;
    }

    public static boolean isZero(Double value) {
        return isZero(value, DOUBLE_THRESHOLD);
    }

    public static boolean isZero(Double value, double threshold) {
        return value != null && value >= -threshold && value <= threshold;
    }

    public static boolean isNonZero(Integer value) {
        return value != null && value != 0;
    }

    public static boolean isNonZero(Long value) {
        return value != null && value != 0L;
    }

    public static boolean isLessOrEqual(LocalDate first, LocalDate second) {
        return first != null && second != null && (first.isEqual(second) || first.isBefore(second));
    }

    public static <T extends Comparable<T>> boolean isLessOrEquals(T first, T second) {
        return first != null && second != null && first.compareTo(second) < 1;
    }

    public static boolean isGreaterThanZero(Long value) {
        return value != null && value > 0;
    }

    public static boolean isGreaterThanOne(Integer value) {
        return value != null && value > 1;
    }

    public static boolean isGreaterOrEqualsThan(Integer first, Integer second) {
        return first != null && second != null && first >= second;
    }

    public static boolean isGreaterThan(Integer first, Integer second) {
        return first != null && second != null && first > second;
    }

    public static boolean isLessOrEqualsThan(Integer first, Integer second) {
        return first != null && second != null && first <= second;
    }

    public static <T extends Comparable<T>> boolean isGreaterThanOrEquals(T first, T second) {
        return first != null && second != null && first.compareTo(second) >= 0;
    }

    public static <T extends Comparable<T>> boolean isGreaterThan(T first, T second) {
        return first != null && second != null && first.compareTo(second) > 0;
    }

    public static <T extends Comparable<T>> boolean isEquals(T first, T second) {
        return first != null && second != null && first.compareTo(second) == 0;
    }

    public static <T extends Comparable<T>> boolean isNotEquals(T first, T second) {
        return !isEquals(first, second);
    }

    public static boolean isNullOrEqualsZero(Number number) {
        return number == null || DoubleMath.fuzzyEquals(number.doubleValue(), 0.0, DOUBLE_THRESHOLD);
    }

    public static boolean isNullOrEqualsZero(Integer integer) {
        return integer == null || integer.equals(0);
    }

    public static boolean isNullOrEqualsZero(Long val) {
        return val == null || val.equals(0L);
    }

    public static boolean isEqualsZero(Number number) {
        return DoubleMath.fuzzyEquals(number.doubleValue(), 0.0, DOUBLE_THRESHOLD);
    }

    public static boolean isNotNullAndNotEqualsZero(Number number) {
        return !isNullOrEqualsZero(number);
    }

    public static boolean isNullOrEqualsZero(LocalDate date) {
        return date == null || ZERO_DATE.equals(date);
    }

    public static boolean isNotNullAndNotEqualsZero(LocalDate date) {
        return !isNullOrEqualsZero(date);
    }

    public static boolean isGreaterThanZero(Integer value) {
        return value != null && value > 0;
    }

    public static boolean isLessThanZero(Integer value) {
        return value != null && value < 0;
    }

    public static Integer ensureRange(Integer value, Integer min, Integer max) {
        return Math.min(Math.max(value, min), max);
    }

    @SafeVarargs
    public static <T> boolean isAnyOf(T value, T... values) {
        if (values == null) {
            return false;
        }
        for (T someValue : values) {
            if (someValue == null) {
                return value == null;
            }
            if (someValue.equals(value)) {
                return true;
            }
        }
        return false;
    }

    @SafeVarargs
    public static <T> boolean isNoneOf(T value, T... values) {
        if (values == null) {
            return true;
        }
        for (T someValue : values) {
            if (someValue == null) {
                if (value == null) {
                    return false;
                }
            } else if (someValue.equals(value)) {
                return false;
            }
        }
        return true;
    }

    @SafeVarargs
    public static <T> boolean isAllOf(T value, T... values) {
        if (values == null) {
            return false;
        }
        for (T someValue : values) {
            if (!someValue.equals(value)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAnyNull(Object... values) {
        if (values == null) {
            return true;
        }
        for (Object value : values) {
            if (value == null) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAllNotNull(Object... values) {
        if (values == null) {
            return false;
        }
        for (Object value : values) {
            if (value == null) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getGenericParamClass(Class<?> genericClass) {
        ParameterizedType pt = getConverterGenericSuperclass(genericClass);
        Type[] args = pt.getActualTypeArguments();
        if (args.length == 0) {
            throw new IllegalArgumentException(buildMessageByClass(genericClass));
        }
        return (Class<T>) pt.getActualTypeArguments()[0];
    }

    @SuppressWarnings({"checkstyle:com.puppycrawl.tools.checkstyle.checks.coding.IllegalInstantiationCheck"})
    private static ParameterizedType getConverterGenericSuperclass(Class<?> genericClass) {
        Type superclass = genericClass.getGenericSuperclass();
        if (superclass instanceof ParameterizedType) {
            return (ParameterizedType) superclass;
        }

        throw new IllegalArgumentException(buildMessageByClass(genericClass));
    }

    private static String buildMessageByClass(Class<?> genericClass) {
        return String.format("Class [%s] should extend parent with defined generic types.",
                genericClass.getSimpleName());
    }

    public static void assertIsNoMoreOneElement(List<?>... lists) {
        if (ArrayUtils.isEmpty(lists)) {
            return;
        }

        for (List<?> list : lists) {
            if (!CollectionUtils.isEmpty(list) && list.size() > 1) {
                throw new IllegalStateException("More than one result found. Not unique element.");
            }
        }
    }

    public static boolean fuzzyEquals(float n1, float n2) {
        return DoubleMath.fuzzyEquals(n1, n2, DOUBLE_THRESHOLD);
    }

    public static boolean fuzzyEquals(double n1, double n2) {
        return DoubleMath.fuzzyEquals(n1, n2, DOUBLE_THRESHOLD);
    }

    public static Long toLong(String codeString) {
        try {
            return Long.valueOf(codeString);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Integer toIntOrZero(Integer value) {
        return value != null ? value : 0;
    }

    public static Integer toIntOrZero(String value) {
        return StringUtils.isBlank(value) ? 0 : Integer.parseInt(value.trim());
    }

    public static Integer toIntValueOrZero(Long value) {
        return value != null ? value.intValue() : 0;
    }

    public static Long toLong(Integer val) {
        return mapValue(val, Integer::longValue);
    }

    public static Double toDouble(Integer val) {
        return mapValue(val, Integer::doubleValue);
    }

    public static Double toDouble(Long val) {
        return mapValue(val, Long::doubleValue);
    }

    public static <T, R> R mapValue(T value, Function<T, R> mapper) {
        return mapValueOrDefault(value, mapper, null);
    }

    public static <T, R> R mapValueOrDefault(T value, Function<T, R> mapper, R defaultValue) {
        return value != null ? mapper.apply(value) : defaultValue;
    }

    public static List<Long> toListOfLong(List<Integer> inputList) {
        return toMappedList(inputList, Integer::longValue);
    }

    public static <T, R> List<R> toMappedList(List<T> inputList, Function<T, R> mapper) {
        if (inputList == null) {
            return Collections.emptyList();
        }
        List<R> resultList = new ArrayList<>(inputList.size());
        for (T val : inputList) {
            resultList.add(val != null ? mapper.apply(val) : null);
        }
        return resultList;
    }

    public static Integer toIntOrDefault(BigDecimal bd, Integer defaultValue) {
        return mapValueOrDefault(bd, BigDecimal::intValue, defaultValue);
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = BigDecimal.valueOf(value);
        return BigDecimalUtils.round(bd, places).doubleValue();
    }

    public static int[] arrayRoundToInt(BigDecimal[] values) {
        int[] result = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = BigDecimalUtils.roundToInt(values[i]);
        }
        return result;
    }

    public static int divideWithRoundToInt(Integer value, Integer divisor) {
        return BigDecimalUtils.roundToInt(BigDecimalUtils.divideAsBigDecimals(value, divisor));
    }

    public static <T> T getOrDefault(T obj, T defaultVal) {
        return obj != null ? obj : defaultVal;
    }

    public static <T> T getOrDefault(T value, Supplier<T> supplier) {
        return (value != null) ? value : supplier.get();
    }

    public static <T, R> R getOrDefault(T value, Function<T, R> converter, R defaultValue) {
        return (value != null) ? converter.apply(value) : defaultValue;
    }

    public static <T> String toStringOrDefault(T obj, T defaultVal) {
        return obj != null ? obj.toString() : toStringOrEmpty(defaultVal);
    }

    public static <T> String toStringOrEmpty(T obj) {
        return obj != null ? obj.toString() : EMPTY_STRING;
    }

    public static String nonNullOrNonZeroToString(Number n, String defaultValue) {
        return isNullOrEqualsZero(n) ? defaultValue : n.toString();
    }

    public static <T> String toStringOrNull(T obj) {
        return obj != null ? obj.toString() : null;
    }

    public static int sum(Integer... values) {
        return Stream.of(values)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum();
    }

    public static int sum(List<Integer> values) {
        if (CollectionUtils.isEmpty(values)) {
            return 0;
        }
        return values.stream()
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum();
    }

    public static long sum(Long... values) {
        return Stream.of(values)
                .filter(Objects::nonNull)
                .mapToLong(Long::longValue)
                .sum();
    }

    public static boolean bulkAnd(Boolean... values) {
        return values.length > 0 && Stream.of(values)
                .allMatch(o -> o != null && o);
    }

    public static boolean bulkOr(Boolean... values) {
        return values.length > 0 && Stream.of(values)
                .anyMatch(o -> o != null && o);
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
    public static long sum(List<Long> values, int start, int end) {
        return BigDecimalUtils.sumBigDecimalsSafe(values.stream().map(BigDecimal::new).collect(Collectors.toList()),
                start, end)
                .longValue();
    }

    public static Integer sanitizeSearchNumber(Integer value) {
        return value == null || value == 0 ? null : value;
    }

    public static Long sanitizeSearchNumber(Long value) {
        return value == null || value.equals(0L) ? null : value;
    }

    public static boolean isBeforeOrEq(LocalDate d1, LocalDate d2) {
        return !(d1 == null || d2 == null) && !d1.isAfter(d2);
    }

    public static boolean isAfterOrEq(LocalDate d1, LocalDate d2) {
        return !(d1 == null || d2 == null) && !d1.isBefore(d2);
    }

    public static boolean isAfter(LocalDate d1, LocalDate d2) {
        return !(d1 == null || d2 == null) && d1.isAfter(d2);
    }

    /**
     * @param left  Inclusive left bound
     * @param right Inclusive right bound
     */
    public static boolean isBetween(LocalDate date, LocalDate left, LocalDate right) {
        return isBeforeOrEq(left, date) && isBeforeOrEq(date, right);
    }

    public static boolean isBefore(LocalDate d1, LocalDate d2) {
        return !(d1 == null || d2 == null) && d1.isBefore(d2);
    }

    public static <T> boolean isBetweenOrEq(T min, Comparable<T> value, T max) {
        boolean valueToMinGreaterThanOrEq = value.compareTo(min) >= 0;
        boolean valueToMaxLessThanOrEq = value.compareTo(max) <= 0;
        return valueToMinGreaterThanOrEq && valueToMaxLessThanOrEq;
    }

    public static String addLeadingZeros(String str, int size) {
        return StringUtils.leftPad(str, size, ZERO);
    }

    public static String addLeadingWhitespaces(String str, int size) {
        return StringUtils.leftPad(str, size, StringUtils.SPACE);
    }

    public static String removeLeadingZeros(String value) {
        return StringUtils.stripStart(value, ZERO);
    }

    public static int getDealClassification(int dealNumber) {
        return (dealNumber % 1000) / 100;
    }

    public static int getDealClassification(long dealNumber) {
        return getDealClassification((int) dealNumber);
    }

    public static String equitySubstring(String str, int start, int length) {
        if (start >= str.length()) {
            return StringUtils.EMPTY;
        }
        return str.substring(start, Math.min(str.length(), start + length));
    }

    public static String safeSubstring(String s, int startIndex, int endIndex) {
        boolean isIndexesNotValid = startIndex < 0 || endIndex <= 0 || startIndex > endIndex;
        if (s == null || s.isEmpty() || isIndexesNotValid || startIndex >= s.length()) {
            return "";
        }
        return endIndex >= s.length() ? s.substring(startIndex) : s.substring(startIndex, endIndex);
    }

    public static boolean bothEmptyOrEquals(String str1, String str2) {
        if (StringUtils.isEmpty(str1)) {
            return StringUtils.isEmpty(str2);
        }
        return str1.equals(str2);
    }

    public static boolean bothBlankOrEquals(String str1, String str2) {
        if (StringUtils.isBlank(str1)) {
            return StringUtils.isBlank(str2);
        }
        return str1.equals(str2);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getProperty(Object bean, String propertyName) {
        try {
            return (T) BeanUtils.getPropertyDescriptor(bean.getClass(), propertyName).getReadMethod().invoke(bean);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Error reading property " + propertyName + ON_OBJECT + bean, e);
        }
    }

    public static void setProperty(Object bean, String property, Object value) {
        try {
            BeanUtils.getPropertyDescriptor(bean.getClass(), property).getWriteMethod().invoke(bean, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Error writing property " + property + ON_OBJECT + bean
                    + " value " + value, e);

        }
    }

    public static boolean isValidDate(LocalDate date) {
        return date != null && !ZERO_DATE.equals(date) && !MAX_DATE.equals(date);
    }

    public static boolean bothZeroOrEquals(Integer value1, Integer value2) {
        if (value1 == null || value1 == 0) {
            return value2 == null || value2 == 0;
        }
        return value1.equals(value2);
    }

    public static Integer zeroIfNegative(Integer value) {
        if (value == null) {
            return 0;
        }
        return value < 0 ? 0 : value;
    }

    /**
     * Check if first symbol of location code is numeric.
     *
     * <ul>
     * <li>"0BD033"  ->   true  </li>
     * <li>"WEE033"  ->   false </li>
     * <li>"   "     ->   false </li>
     * <li>""        ->   false </li>
     * <li>null      ->   false </li>
     * </ul>
     */
    public static boolean isFirstDigit(String locationCode) {
        return !StringUtils.isBlank(locationCode) && Character.isDigit(locationCode.charAt(0));
    }

    public static <T> T getFirstOrNull(Iterable<T> iterable) {
        return Iterables.getFirst(iterable, null);
    }

    public static Long zeroIfNegative(Long value) {
        if (value == null) {
            return 0L;
        }
        return value < 0 ? 0L : value;
    }

    public static String escapeCrlfSequence(Object objectToLog) {
        return String.valueOf(objectToLog).replaceAll("[\r\n]", "");
    }

    public static Long valueOrZero(Long value) {
        return Optional.ofNullable(value).orElse(ZERO_LONG);
    }

    public static String valueOrEmpty(String value) {
        return Optional.ofNullable(value).orElse(EMPTY_STRING);
    }

    public static String zeroIfNullOrEmpty(String value){
        return value == null || EMPTY_STRING.equals(value) ? ZERO : value;
    }
    public static String hideSensitiveData(String src, String overlay, int lastVisibleSimbols) {
        int end = src.length() - lastVisibleSimbols;
        return StringUtils.overlay(src, StringUtils.repeat(overlay, end), 0, end);
    }

    public static <E, P> P getPropertyOrDefault(E instance, Function<E, P> propertyGetter, P defaultValue) {
        return Optional
                .ofNullable(instance)
                .map(propertyGetter)
                .orElse(defaultValue);
    }

    public static <T> List<T> sort(Collection<T> items, Comparator<T> comparator) {
        List<T> copy = new ArrayList<>(items);
        copy.sort(comparator);
        return copy;
    }

    public static void cropFieldToDecimal5(Supplier<Integer> getter, Consumer<Integer> setter) {
        cropField(getter, setter, DECIMAL_5_MIN, DECIMAL_5_MAX);
    }

    public static <T> Collection<T> concat(Collection<T> coll1, Collection<T> coll2) {
        Collection<T> result = new ArrayList<>(coll1.size() + coll2.size());
        result.addAll(coll1);
        result.addAll(coll2);
        return result;
    }


    @SuppressWarnings("unchecked")
    public static <T extends Number> T trimFromLeft(Number val, int length) {
        if (val == null) {
            return null;
        }

        String numberStr = Utils.safeSubstring(String.valueOf(val), 0, length);
        T result;
        try {
            result = (T) val.getClass().getConstructor(String.class).newInstance(numberStr);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException
                | InstantiationException e) {
            throw new IllegalArgumentException(String.format("This class doesn't support construct for %s type", val
                    .getClass().getSimpleName()), e);
        }
        return result;
    }

    public static String trimFromLeft(String val, int length) {
        if (val == null) {
            return null;
        }

        return Utils.safeSubstring(val, 0, length);
    }

    public static boolean isLikeEquityBlank(String value) {
        if (value == null) {
            return true;
        } else {
            String trimmedValue = value.trim();
            return StringUtils.isBlank(trimmedValue) || trimmedValue.matches(ZERO_STRING_PATTERN);
        }
    }

    public static String replaceNonDigitSymbolsToZero(String text) {

        if (text == null){
            return null;
        }

        StringBuilder checkedFieldTexts = new StringBuilder();
        for (Character c : text.toCharArray()) {
            if (Character.isDigit(c)) {
                checkedFieldTexts.append(c);
            } else {
                checkedFieldTexts.append(0);
            }
        }
        return checkedFieldTexts.toString();
    }

    public static Map<String, Object> getAllFieldNamesInSnakeCaseWithValues(final Object obj) {
        Class<?> objClass = obj.getClass();
        Map<String, Object> map = new HashMap<>();
        Field[] fields = objClass.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            try {
                if (Modifier.isPublic(field.getModifiers())) {
                    Object value = field.get(obj);
                    map.put(toSnakeLowerCase(camelCaseToSnakeCase(name)), value);
                } else {
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    map.put(toSnakeLowerCase(camelCaseToSnakeCase(name)), value);
                }
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(String.format(OBJECT_NOT_VALID, obj.getClass()), e);
            }
        }
        return map;
    }

    public static Map<String, Object> getPublicOnlyFieldNamesInSnakeCaseWithValues(final Object obj) {
        Class<?> objClass = obj.getClass();
        Map<String, Object> map = new HashMap<>();
        Field[] fields = objClass.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            try {
                if (Modifier.isPublic(field.getModifiers())) {
                    Object value = field.get(obj);
                    map.put(toSnakeLowerCase(camelCaseToSnakeCase(name)), value);
                }
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(String.format(OBJECT_NOT_VALID, obj.getClass()), e);
            }
        }
        return map;
    }

    public static String camelCaseToSnakeCase(String camelCase) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, camelCase);
    }

    @SuppressWarnings("checkstyle:com.puppycrawl.tools.checkstyle.checks.coding.IllegalInstantiationCheck")
    public static String toSnakeLowerCase(String init) {
        char[] initArray = init.toCharArray();
        if (initArray.length > 0) {
            initArray[0] = Character.toLowerCase(initArray[0]);
        }
        for (int i = 0; i < initArray.length; i++) {
            if (initArray[i] == '_' && i + 1 < initArray.length) {
                initArray[i + 1] = Character.toLowerCase(initArray[i + 1]);
            }
        }

        return new String(initArray);
    }
}
