package mods.clayium.pan;

import mods.clayium.util.crafting.IItemPattern;
import net.minecraft.item.ItemStack;

public interface IPANConversion {
    IItemPattern[] getPatterns();

    ItemStack[] getResults();

    double getEnergy();
}
