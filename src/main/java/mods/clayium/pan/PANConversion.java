package mods.clayium.pan;

import mods.clayium.util.crafting.IItemPattern;
import net.minecraft.item.ItemStack;

public class PANConversion
        implements IPANConversion {
    private IItemPattern[] patterns;
    private ItemStack[] results;
    private double energy;

    public PANConversion(IItemPattern[] patterns, ItemStack[] results, double energy) {
        this.patterns = patterns;
        this.results = results;
        this.energy = energy;
    }


    public IItemPattern[] getPatterns() {
        return this.patterns;
    }


    public ItemStack[] getResults() {
        return this.results;
    }


    public double getEnergy() {
        return this.energy;
    }
}
