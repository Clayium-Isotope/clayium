package mods.clayium.machine;

import mods.clayium.core.ClayiumCore;
import mods.clayium.item.common.ClayiumMaterial;

public enum TierPrefix {
    none("", ClayiumMaterial.clay),
    clay("clay", ClayiumMaterial.clay),
    denseClay("dense_clay", ClayiumMaterial.denseClay),
    simple("simple", ClayiumMaterial.indClay),
    basic("basic", ClayiumMaterial.advClay),
    advanced("advanced", ClayiumMaterial.impureSilicon),
    precision("precision", ClayiumMaterial.mainAluminum),
    claySteel("clay_steel", ClayiumMaterial.claySteel),
    clayium("clayium", ClayiumMaterial.clayium),
    ultimate("ultimate", ClayiumMaterial.ultimateAlloy),
    antimatter("antimatter", ClayiumMaterial.antimatter),
    pureAntimatter("pure_antimatter", ClayiumMaterial.pureAntimatter),
    OEC("oec", ClayiumMaterial.octupleEnergeticClay),
    OPA("opa", ClayiumMaterial.octuplePureAntimatter);

    TierPrefix(String prefix, ClayiumMaterial material) {
        this.prefix = prefix;
        this.material = material;
    }

    public String getPrefix() {
        return prefix;
    }

    public ClayiumMaterial getMaterial() {
        return material;
    }

    public static TierPrefix get(int tier) {
        switch (tier) {
            case 0:
                return none;
            case 1:
                return clay;
            case 2:
                return denseClay;
            case 3:
                return simple;
            case 4:
                return basic;
            case 5:
                return advanced;
            case 6:
                return precision;
            case 7:
                return claySteel;
            case 8:
                return clayium;
            case 9:
                return ultimate;
            case 10:
                return antimatter;
            case 11:
                return pureAntimatter;
            case 12:
                return OEC;
            case 13:
                return OPA;
        }

        ClayiumCore.logger.error(new IllegalAccessException());
        return none;
    }

    private final String prefix;
    private final ClayiumMaterial material;
}
