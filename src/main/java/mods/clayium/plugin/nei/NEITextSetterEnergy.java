package mods.clayium.plugin.nei;

import mods.clayium.gui.FDText;
import mods.clayium.util.UtilLocale;

public class NEITextSetterEnergy<T> extends FDText.FDTextHandler<T> {
    public String applyString(T param) {
        long energy = getEnergy(param);
        long time = getTime(param);
        if (energy < 0L) {
            if (time < 0L)
                return "";
            return UtilLocale.craftTimeNumeral(time) + "t";
        }
        if (time < 0L)
            return UtilLocale.ClayEnergyNumeral(energy) + "CE";
        return UtilLocale.ClayEnergyNumeral(energy) + "CE/t x " + UtilLocale.craftTimeNumeral(time) + "t = " +
                UtilLocale.ClayEnergyNumeral(energy * time) + "CE";
    }

    public long getEnergy(T param) {
        return (param instanceof NEITemp.INEIEntryEnergy) ? ((NEITemp.INEIEntryEnergy) param).getEnergy() : -1L;
    }

    public long getTime(T param) {
        return (param instanceof NEITemp.INEIEntryEnergy) ? ((NEITemp.INEIEntryEnergy) param).getTime() : -1L;
    }
}
