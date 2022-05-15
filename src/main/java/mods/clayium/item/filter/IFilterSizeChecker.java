package mods.clayium.item.filter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IFilterSizeChecker {
    void checkFilterSize(ItemStack paramItemStack, EntityPlayer paramEntityPlayer, World paramWorld);
}
