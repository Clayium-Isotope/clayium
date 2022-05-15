package mods.clayium.item.filter;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public interface IItemFilter extends IFilterSizeChecker, IItemWithFilterSize {
    void addFilterInformation(ItemStack paramItemStack, EntityPlayer paramEntityPlayer, List paramList, boolean paramBoolean);

    void addTooltip(NBTTagCompound paramNBTTagCompound, List paramList, int paramInt);

    boolean filterMatch(NBTTagCompound paramNBTTagCompound, ItemStack paramItemStack);

    boolean filterMatch(NBTTagCompound paramNBTTagCompound, World paramWorld, int paramInt1, int paramInt2, int paramInt3);

    boolean isCopy(ItemStack paramItemStack);

    ItemStack setCopyFlag(ItemStack paramItemStack);

    ItemStack clearCopyFlag(ItemStack paramItemStack);
}
