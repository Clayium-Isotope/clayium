package mods.clayium.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public interface ISpecialFilter {
    boolean filterMatch(NBTTagCompound paramNBTTagCompound, ItemStack paramItemStack, boolean paramBoolean);

    boolean filterMatch(NBTTagCompound paramNBTTagCompound, World paramWorld, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean);

    void addTooltip(NBTTagCompound paramNBTTagCompound, List paramList, int paramInt);

    void openGui(ItemStack paramItemStack, World paramWorld, EntityPlayer paramEntityPlayer);
}
