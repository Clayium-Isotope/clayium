package mods.clayium.pan;

import net.minecraft.world.World;

public interface IPANAdapterConversionFactory {
    boolean match(World paramWorld, int paramInt1, int paramInt2, int paramInt3);

    IPANConversion getConversion(IPANAdapter paramIPANAdapter);
}
