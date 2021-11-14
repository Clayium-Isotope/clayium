package mods.clayium.plugin.nei;

import mods.clayium.gui.FDText;

public class NEITextSetterTier<T> extends FDText.FDTextHandler<T> {
    public String applyString(T param) {
        int tier = getTier(param);
        return (tier < 0) ? "" : ("Tier: " + tier);
    }

    public int getTier(T param) {
        return (param instanceof NEITemp.INEIEntryTiered) ? ((NEITemp.INEIEntryTiered) param).getTier() : -1;
    }
}
