package mods.clayium.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;

import mods.clayium.core.ClayiumCore;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.StatCollector;

public class UtilLocale {
    static String[] CENumerals = new String[] {"u", "m", "", "k", "M", "G", "T", "P", "E", "Z", "Y"};

    public static String ClayEnergyNumeral(double ce, boolean flag) {
        double n = ce * 10.0D;
        String s = "";
        if (n == 0.0D)
            return String.valueOf(n);
        if (n < 0.0D) {
            n--;
            s = "-";
        }
        int k = (int) Math.floor(Math.log10(n));
        int p = Math.min(k / 3, CENumerals.length - 1);
        int d = (int) (n * 1000.0D / Math.pow(10.0D, (p * 3)));


        return s + ClayEnergyNumeral(d, p, (p == 0 || flag));
    }


    public static String ClayEnergyNumeral(double ce) {
        return ClayEnergyNumeral(ce, true);
    }

    public static String ClayEnergyNumeral(long ce, boolean flag) {
        long n = ce * 10L;
        String s = "";
        if (n == 0L)
            return String.valueOf(n);
        if (n < 0L) {
            n--;
            s = "-";
        }
        int k = (int) Math.floor(Math.log10(n));
        int p = Math.min(k / 3, CENumerals.length - 1);
        int d = (int) (n * 1000.0D / Math.pow(10.0D, (p * 3)));
        return s + ClayEnergyNumeral(d, p, (p == 0 || flag));
    }

    public static String ClayEnergyNumeral(long ce) {
        return ClayEnergyNumeral(ce, true);
    }

    protected static String ClayEnergyNumeral(int d, int p, boolean flag) {
        if (d % 10 != 0 || !flag)
            return String.valueOf(d / 1000) + "." + String.valueOf(d / 100 % 10) + String.valueOf(d / 10 % 10) + String.valueOf(d % 10) + CENumerals[p];
        if (d % 100 != 0)
            return String.valueOf(d / 1000) + "." + String.valueOf(d / 100 % 10) + String.valueOf(d / 10 % 10) + CENumerals[p];
        if (d % 1000 != 0) {
            return String.valueOf(d / 1000) + "." + String.valueOf(d / 100 % 10) + CENumerals[p];
        }
        return String.valueOf(d / 1000) + CENumerals[p];
    }


    static String[] SNumerals = new String[] {"", "k", "M", "G", "T", "P", "E", "Z", "Y"};
    private static final int maxLineTooltip = 12;

    protected static String StackSizeNumeral(long stackSize, boolean flag) {
        String s = "";
        long n = stackSize;
        if (n == 0L)
            return String.valueOf(n);
        if (n < 0L) {
            n--;
            s = "-";
        }
        int k = (int) Math.floor(Math.log10(n));
        if (k < 5) return s + String.valueOf(n);

        int p = Math.min(k / 3, SNumerals.length - 1);
        int d = (int) (n / Math.pow(10.0D, (k - 2)));


        boolean flag1 = (flag && k % 3 <= 1 && d % 10 == 0);
        boolean flag2 = (flag1 && k % 3 == 0 && d / 10 % 10 == 0);
        return s + String.valueOf(d / 100) + (flag2 ? "" : (((k % 3 == 0) ? "." : "") +
                String.valueOf(d / 10 % 10))) + (flag1 ? "" : (((k % 3 == 1) ? "." : "") +
                String.valueOf(d % 10))) + SNumerals[p];
    }


    public static String StackSizeNumeral(long stackSize) {
        return StackSizeNumeral(stackSize, false);
    }

    public static String CAResonanceNumeral(double resonance) {
        if (resonance < 1.0D)
            return String.format("%.4f", new Object[] {Double.valueOf(resonance)});
        if (resonance < 10.0D)
            return String.format("%.3f", new Object[] {Double.valueOf(resonance)});
        if (resonance < 100.0D)
            return String.format("%.2f", new Object[] {Double.valueOf(resonance)});
        if (resonance < 1000.0D)
            return String.format("%.1f", new Object[] {Double.valueOf(resonance)});
        return StackSizeNumeral((long) resonance);
    }

    public static String laserNumeral(long laser) {
        return StackSizeNumeral(laser);
    }

    @SideOnly(Side.CLIENT)
    public static String laserGui(long laser) {
        return I18n.format("gui.Common.clayLaser", new Object[] {laserNumeral(laser)});
    }

    @SideOnly(Side.CLIENT)
    public static String tierGui(int tier) {
        return I18n.format("gui.Common.tier", new Object[] {Integer.valueOf(tier)});
    }


    public static String craftTimeNumeral(long craftTime) {
        return StackSizeNumeral(craftTime, true);
    }

    public static String rfNumeral(long rf) {
        return String.format("%,d", new Object[] {Long.valueOf(rf)});
    }


    public static String localizeUnsafe(String str) {
        String ret = StatCollector.translateToLocal(str);
        if (ret != null && ret != "" && !ret.equals(str))
            return ret;
        return null;
    }


    public static String localizeAndFormat(String str, Object... args) {
        String ret = localizeUnsafe(str);
        if (ret == null)
            return null;
        try {
            ret = String.format(ret, args);
        } catch (IllegalFormatException e) {
            ClayiumCore.logger.catching(e);
            return str;
        }
        return ret;
    }

    public static boolean canLocalize(String str) {
        return StatCollector.canTranslate(str);
    }


    public static List<String> localizeTooltip(String str) {
        boolean flag = true;
        int i = 0;
        List<String> ret = new ArrayList<String>();
        while (flag && i < 12) {
            i++;
            String loc = localizeUnsafe(str + ".line" + i);
            if (loc != null) {
                if (loc.equals("__DETAIL__")) {
                    if (!GuiScreen.isShiftKeyDown()) {
                        ret.add(localizeUnsafe("tooltip.HoldShiftForDetails"));
                        flag = false;
                    }
                    continue;
                }
                ret.add(loc);
                continue;
            }
            flag = false;
        }

        return ret;
    }
}


