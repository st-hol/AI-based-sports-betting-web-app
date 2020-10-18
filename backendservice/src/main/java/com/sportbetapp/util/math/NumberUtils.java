package com.sportbetapp.util.math;

@SuppressWarnings({"frontier-java:MathOnBigNumbers", "frontier-java:AvoidToString", "frontier-java:MathOnBoxedNumbers"})
public final class NumberUtils {

    public static final int TEN = 10;

    private NumberUtils() {}

    public static int unbox(Integer value) {
        return value == null ? 0 : value;
    }

    public static long unbox(Long value) {
        return value == null ? 0L : value;
    }

    public static short unbox(Short value) {
        return value == null ? (short) 0 : value;
    }

    public static float unbox(Float value) {
        return value == null ? 0.0F : value;
    }

    public static double unbox(Double value) {
        return value == null ? 0.0D : value;
    }

    public static byte unbox(Byte value) {
        return value == null ? (byte) 0 : value;
    }

    /**
     * This method is meant to round in equity style
     *
     * @param value     actual value to be scaled
     * @param precision expected value precision
     * @return rounded value
     *
     *
     * <br>(12, 3) ==> 12
     * <br>(1, 1) ==> 1
     * <br>(9999, 3) ==> 999
     * <br>(-10712, 4) ==> -712
     * <br>(-117, 2) ==> -17
     */
    public static int equityRound(Integer value, int precision) {
        int number = unbox(value);
        int div = (int) Math.pow(TEN, precision);

        return number % div;
    }

    /**
     * This method is meant to round in equity style
     *
     * @param value     actual value to be scaled
     * @param precision expected value precision
     * @return rounded value
     *
     *
     * <br>(12, 3) ==> 12
     * <br>(1, 1) ==> 1
     * <br>(9999, 3) ==> 999
     * <br>(-10712, 4) ==> -712
     * <br>(-117, 2) ==> -17
     */
    public static long equityRound(Long value, int precision) {
        long number = unbox(value);
        int div = (int) Math.pow(TEN, precision);

        return number % div;
    }

}
