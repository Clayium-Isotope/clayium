package mods.clayium.util;

public class RangeCheck {

    /**
     * @return true when num is in [min, max]
     */
    public static boolean inclusive(int num, int min, int max) {
        if (min > max) return max <= num && num <= min;

        return min <= num && num <= max;
    }

    /**
     * @return true when num is in )min, max(
     */
    public static boolean isInOutOfInclusive(int num, int min, int max) {
        if (min > max) return min < num || num < max;

        return max < num || num < min;
    }
}
