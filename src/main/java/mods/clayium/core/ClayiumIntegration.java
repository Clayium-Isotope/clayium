package mods.clayium.core;

import net.minecraftforge.fml.common.Loader;

public enum ClayiumIntegration {
    JEI("jei", "jei"),
    ;

    public final boolean isMod;
    public final String modId;
    public final String configId;

    ClayiumIntegration(String modId, String configId) {
        this.isMod = true;
        this.modId = modId;
        this.configId = configId;
    }

    ClayiumIntegration(String configId) {
        this.isMod = false;
        this.modId = "";
        this.configId = configId;
    }

    public boolean enabled() {
        Boolean b = ClayiumConfiguration.cfgModIntegration.get(this);
        return b != null && b;
    }

    public boolean loaded() {
        return /*enabled() &&*/ this.isMod && Loader.isModLoaded(this.modId);
    }
}
