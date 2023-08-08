package mods.clayium.util;

import mods.clayium.item.common.ClayiumMaterial;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public enum TierPrefix {
    unknown("", ClayiumMaterial.clayium),
    none("", ClayiumMaterial.clay),
    clay("clay", ClayiumMaterial.clay),
    denseClay("dense_clay", ClayiumMaterial.denseClay),
    simple("simple", ClayiumMaterial.indClay),
    basic("basic", ClayiumMaterial.advClay),
    advanced("advanced", ClayiumMaterial.impureSilicon),
    precision("precision", ClayiumMaterial.mainAluminium),
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

    public int meta() {
        return this.ordinal() - 1;
    }

    private final String prefix;
    private final ClayiumMaterial material;

    public static final Comparator<TierPrefix> comparator = Comparator.comparingInt(TierPrefix::meta);

    public TierPrefix offset(int delta) {
        return TierPrefix.get(this.meta() + delta);
    }

    public boolean isValid() {
        return this != unknown;
    }

    public static TierPrefix get(int rawTier) {
        switch (rawTier) {
            case 0:     return none;
            case 1:     return clay;
            case 2:     return denseClay;
            case 3:     return simple;
            case 4:     return basic;
            case 5:     return advanced;
            case 6:     return precision;
            case 7:     return claySteel;
            case 8:     return clayium;
            case 9:     return ultimate;
            case 10:    return antimatter;
            case 11:    return pureAntimatter;
            case 12:    return OEC;
            case 13:    return OPA;
            default:    return unknown;
        }
    }

    public static List<TierPrefix> makeList(int ...tiers) {
        return Arrays.stream(tiers)
                .mapToObj(TierPrefix::get)
                .collect(Collectors.toList());
    }

    public static final TierPrefix MAX_TIER = OPA;

    public static Iterable<TierPrefix> makeIterable(TierPrefix begin, int offset) {
        return () -> new Iterator<TierPrefix>() {
            private TierPrefix current = begin;

            @Override
            public boolean hasNext() {
                return this.current.isValid();
            }

            @Override
            public TierPrefix next() {
                this.current = current.offset(offset);
                return this.current;
            }
        };
    }
}
