package com.sportbetapp.util.math;

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

}
