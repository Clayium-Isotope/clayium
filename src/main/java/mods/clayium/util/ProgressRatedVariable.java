package mods.clayium.util;

public class ProgressRatedVariable {
    private static double cfgProgressionRate = 1.0D;

    public void setProgressionRate(double v) {
        cfgProgressionRate = v;
    }

    public static double multiplyProgressionRateD(double a) {
        return a * cfgProgressionRate;
    }

    public static float multiplyProgressionRateF(float a) {
        return (float)((double)a * cfgProgressionRate);
    }

    public static int multiplyProgressionRateI(int a) {
        int r = (int)((double)a * cfgProgressionRate);
        return r != 0 ? r : (Integer.compare(a, 0));
    }

    public static long multiplyProgressionRateL(long a) {
        long r = (long)((double)a * cfgProgressionRate);
        return r != 0L ? r : (a < 0L ? -1L : (a > 0L ? 1L : 0L));
    }

    public static int multiplyProgressionRateStackSize(int a) {
        int r = multiplyProgressionRateI(a);
        return Math.min(r, 64);
    }

    public static double divideByProgressionRateD(double a) {
        return a / cfgProgressionRate;
    }

    public static float divideByProgressionRateF(float a) {
        return (float)((double)a / cfgProgressionRate);
    }

    public static int divideByProgressionRateI(int a) {
        int r = (int)((double)a / cfgProgressionRate);
        return r != 0 ? r : (Integer.compare(a, 0));
    }

    public static long divideByProgressionRateL(long a) {
        long r = (long)((double)a / cfgProgressionRate);
        return r != 0L ? r : (a < 0L ? -1L : (a > 0L ? 1L : 0L));
    }

    public static int divideByProgressionRateStackSize(int a) {
        int r = divideByProgressionRateI(a);
        return Math.min(r, 64);
    }
}
