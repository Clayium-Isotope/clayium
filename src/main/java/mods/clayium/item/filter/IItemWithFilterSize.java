package mods.clayium.item.filter;

import net.minecraft.nbt.NBTTagCompound;

public interface IItemWithFilterSize {
    int getFilterSize(NBTTagCompound paramNBTTagCompound);
}
