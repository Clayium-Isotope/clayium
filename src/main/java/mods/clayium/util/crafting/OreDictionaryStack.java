package mods.clayium.util.crafting;

import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryStack {
    private final int id;
    private final int stackSize;

    public OreDictionaryStack(int id, int stackSize) {
        this.id = id;
        this.stackSize = stackSize;
    }

    public OreDictionaryStack(String oreName, int stackSize) {
        this(OreDictionary.getOreID(oreName), stackSize);
    }

    public String getOreName() {
        return OreDictionary.getOreName(this.id);
    }

    public int getId() {
        return id;
    }

    public int getStackSize() {
        return stackSize;
    }
}
