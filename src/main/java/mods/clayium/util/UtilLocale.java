package mods.clayium.util;

import mods.clayium.core.ClayiumCore;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class UtilLocale {
    public static final SIFormat ceFormatWithZero = new SIFormat(true, -5);
    public static final SIFormat ceFormatWoutZero = new SIFormat(false, -5);
    public static final SIFormat numFormatWithZero = new SIFormat(true, 0);
    public static final SIFormat numFormatWoutZero = new SIFormat(false, 0);

    private static final int maxLineTooltip = 12;

    public static String ClayEnergyNumeral(double ce, boolean lessInfo) {
        return (lessInfo ? ceFormatWoutZero : ceFormatWithZero).format(ce);
    }

    public static String ClayEnergyNumeral(double ce) {
        return ClayEnergyNumeral(ce, true);
    }

    public static String ClayEnergyNumeral(long ce, boolean lessInfo) {
        return (lessInfo ? ceFormatWoutZero : ceFormatWithZero).format(ce);
    }

    public static String ClayEnergyNumeral(long ce) {
        return ClayEnergyNumeral(ce, true);
    }

    protected static String StackSizeNumeral(long stackSize, boolean flag) {
        return (flag ? numFormatWoutZero : numFormatWithZero).format(stackSize);
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

        return resonance < 1000.0D ? String.format("%.1f", resonance) : StackSizeNumeral((long) resonance);
    }

    public static String laserNumeral(long laser) {
        return StackSizeNumeral(laser);
    }

    @SideOnly(Side.CLIENT)
    public static String laserGui(long laser) {
        return I18n.format("gui.Common.clayLaser", laserNumeral(laser));
    }

    @SideOnly(Side.CLIENT)
    public static String tierGui(TierPrefix tier) {
        return I18n.format("gui.Common.tier", tier.meta());
    }

    public static String craftTimeNumeral(long craftTime) {
        return StackSizeNumeral(craftTime, true);
    }

    public static String rfNumeral(long rf) {
        return String.format("%,d", rf);
    }

    public static void localizeTooltip(List<String> tooltip, String name, Object... args) {
        String keyBase = name;
        if (name.startsWith("tooltip.")) {
            keyBase += ".line";
        } else {
            keyBase += ".tooltip.line";
        }

        String key;
        String loc;
        for (int i = 1; i < maxLineTooltip; i++) {
            key = keyBase + i;
            loc = I18n.format(key, args);

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

    public static String localizeAndFormat(String key, Object... args) {
        String unsafe = I18n.format(key, args);

        if (unsafe.equals(key))
            return key;

        // this line is uncomfortable
        if (unsafe.startsWith("Format error: ")) {
            ClayiumCore.logger.error("[Illegal Format Exception] Translation Key: " + key);
            return key;
        }

        return unsafe;
    }

    public static ITextComponent localizeAndFormatComponent(String key, Object... args) {
        String unsafe = I18n.format(key, args);

        if (unsafe.equals(key))
            return new TextComponentString(key);

        // this line is uncomfortable
        if (unsafe.startsWith("Format error: ")) {
            ClayiumCore.logger.error("[Illegal Format Exception] Translation Key: " + key);
            return new TextComponentString(key);
        }

        return new TextComponentString(unsafe);
    }

    @Deprecated // for Server
    public static String getLocalizedText(String key, Object... args) {
        return getLocalizedTextComponent(key, args).getFormattedText().trim();
    }

    @Deprecated // for Server
    public static TextComponentTranslation getLocalizedTextComponent(String key, Object... args) {
        return new TextComponentTranslation(key, args);
    }
}
