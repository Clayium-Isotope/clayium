package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import mods.clayium.item.filter.IFilterSizeChecker;
import mods.clayium.item.filter.IItemWithFilterSize;
import mods.clayium.item.filter.ItemFilterWhitelist;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemGadget extends ItemDamaged implements IFilterSizeChecker, IItemWithFilterSize {
    public static ItemFilterWhitelist filterForAutoEat = (ItemFilterWhitelist) CItems.itemFilterWhitelist;


    public int getFilterSize(NBTTagCompound filterTag) {
        return filterForAutoEat.getFilterSize(filterTag);
    }


    public void checkFilterSize(ItemStack filter, EntityPlayer player, World world) {
        filterForAutoEat.checkFilterSize(filter, player, world);
    }

    public static class ItemCallbackItemFilterGui extends ItemCallbackDefault {
        public ItemCallbackItemFilterGui(ItemDamaged cb) {
            cb.super();
        }

        public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
            player.openGui(ClayiumCore.INSTANCE, 20, world, (int) player.posX, (int) player.posY, (int) player.posZ);
            return itemstack;
        }
    }
}
