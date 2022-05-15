package mods.clayium.plugin.nei;

import net.minecraft.item.ItemStack;

import java.util.List;

public class NEIRecipeEntry extends NEITemplateEntry implements NEITemp.INEIEntryTiered, NEITemp.INEIEntryEnergy {
    public int method;
    public int tier;
    public long energy;
    public long time;

    public NEIRecipeEntry(Object[] inputs, int method, int tier, ItemStack[] results, long energy, long time) {
        super(inputs, results);
        this.method = method;
        this.tier = tier;
        this.energy = energy;
        this.time = time;
    }

    public NEIRecipeEntry(List<Object> inputs, int method, int tier, List<ItemStack> results, long energy, long time) { this(inputs.toArray(new Object[0]), method, tier, results.<ItemStack>toArray(new ItemStack[0]), energy, time); }

    public long getEnergy() { return this.energy; }

    public long getTime() { return this.time; }

    public int getTier() { return this.tier; }
}
