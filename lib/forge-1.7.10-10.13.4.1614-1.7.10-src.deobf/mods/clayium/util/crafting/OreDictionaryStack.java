package mods.clayium.util.crafting;

import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryStack {
    public int id;

    public OreDictionaryStack(int id, int stackSize) {
        this.id = id;
        this.stackSize = stackSize;
    }

    public int stackSize;

    public OreDictionaryStack(String oreName, int stackSize) {
        this(OreDictionary.getOreID(oreName), stackSize);
    }

    public String getOreName() {
        return OreDictionary.getOreName(this.id);
    }
}


