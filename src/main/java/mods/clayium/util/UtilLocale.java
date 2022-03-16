package mods.clayium.util;

import mods.clayium.core.ClayiumCore;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.IllegalFormatException;
import java.util.List;

public class UtilLocale {
    static String[] CENumerals = new String[]{"u", "m", "", "k", "M", "G", "T", "P", "E", "Z", "Y"};
    static String[] SNumerals = new String[]{"", "k", "M", "G", "T", "P", "E", "Z", "Y"};
    private static final int maxLineTooltip = 12;

    public UtilLocale() {}

    public static String ClayEnergyNumeral(double ce, boolean flag) {
        double n = ce * 10.0D;
        String s = "";

        if (n == 0.0D) {
            return String.valueOf(n);
        }

        if (n < 0.0D) {
            --n;
            s = "-";
        }

        int k = (int)Math.floor(Math.log10(n));
        int p = Math.min(k / 3, CENumerals.length - 1);
        int d = (int)(n * 1000.0D / Math.pow(10.0D, (double)(p * 3)));

        return s + ClayEnergyNumeral(d, p, p == 0 || flag);
    }

    public static String ClayEnergyNumeral(double ce) {
        return ClayEnergyNumeral(ce, true);
    }

    public static String ClayEnergyNumeral(long ce, boolean flag) {
        long n = ce * 10L;
        String s = "";

        if (n == 0L) {
            return String.valueOf(n);
        }

        if (n < 0L) {
            --n;
            s = "-";
        }

        int k = (int)Math.floor(Math.log10((double)n));
        int p = Math.min(k / 3, CENumerals.length - 1);
        int d = (int)((double)n * 1000.0D / Math.pow(10.0D, (double)(p * 3)));

        return s + ClayEnergyNumeral(d, p, p == 0 || flag);
    }

    public static String ClayEnergyNumeral(long ce) {
        return ClayEnergyNumeral(ce, true);
    }

    protected static String ClayEnergyNumeral(int d, int p, boolean flag) {
        if (d % 10 == 0 && flag) {
            if (d % 100 != 0) {
                return d / 1000 + "." + d / 100 % 10 + d / 10 % 10 + CENumerals[p];
            }

            return d % 1000 != 0 ? d / 1000 + "." + d / 100 % 10 + CENumerals[p] : d / 1000 + CENumerals[p];
        }

        return d / 1000 + "." + d / 100 % 10 + d / 10 % 10 + d % 10 + CENumerals[p];
    }

    protected static String StackSizeNumeral(long stackSize, boolean flag) {
        String s = "";
        long n = stackSize;

        if (stackSize == 0L) {
            return String.valueOf(stackSize);
        }

        if (stackSize < 0L) {
            n = stackSize - 1L;
            s = "-";
        }

        int k = (int)Math.floor(Math.log10((double)n));
        if (k < 5) {
            return s + String.valueOf(n);
        }

        int p = Math.min(k / 3, SNumerals.length - 1);
        int d = (int)((double)n / Math.pow(10.0D, (double)(k - 2)));
        boolean flag1 = flag && k % 3 <= 1 && d % 10 == 0;
        boolean flag2 = flag1 && k % 3 == 0 && d / 10 % 10 == 0;

        return s + (d / 100) + (flag2 ? "" : (k % 3 == 0 ? "." : "") + (d / 10 % 10)) + (flag1 ? "" : (k % 3 == 1 ? "." : "") + (d % 10)) + SNumerals[p];
    }

    public static String StackSizeNumeral(long stackSize) {
        return StackSizeNumeral(stackSize, false);
    }

    public static String CAResonanceNumeral(double resonance) {
        if (resonance < 1.0D) {
            return String.format("%.4f", resonance);
        }

        if (resonance < 10.0D) {
            return String.format("%.3f", resonance);
        }

        if (resonance < 100.0D) {
            return String.format("%.2f", resonance);
        }

        return resonance < 1000.0D ? String.format("%.1f", resonance) : StackSizeNumeral((long)resonance);
    }

    public static String laserNumeral(long laser) {
        return StackSizeNumeral(laser);
    }

    @SideOnly( Side.CLIENT)
    public static String laserGui(long laser) {
        return I18n.format("gui.Common.clayLaser", laserNumeral(laser));
    }

    @SideOnly(Side.CLIENT)
    public static String tierGui(int tier) {
        return I18n.format("gui.Common.tier", tier);
    }

    public static String craftTimeNumeral(long craftTime) {
        return StackSizeNumeral(craftTime, true);
    }

    public static String rfNumeral(long rf) {
        return String.format("%,d", rf);
    }

    public static void localizeTooltip(List<String> tooltip, String name, Object... args) {
        for (int i = 1; i < maxLineTooltip; i++) {
            String key = name + ".tooltip.line" + i;
            String loc = I18n.format(key, args);

            if (loc.equals(key) || loc.equals("")) return;

            if (loc.equals("__DETAIL__")) {
                if (!GuiScreen.isShiftKeyDown()) {
                    tooltip.add(I18n.format("tooltip.HoldShiftForDetails"));
                    return;
                }
            } else {
                tooltip.add(loc);
            }
        }
    }

    public static boolean canLocalize(String key) {
        return I18n.hasKey(key);
    }

    public static String localizeAndFormat(String key, Object ...args) {
        String unsafe = I18n.format(key, args);

        if (unsafe.equals(key))
            return null;

        // this line is uncomfortable
        if (unsafe.startsWith("Format error: ")) {
            ClayiumCore.logger.error("[Illegal Format Exception] Translation Key: " + key);
            return key;
        }

        return unsafe;
    }
}
