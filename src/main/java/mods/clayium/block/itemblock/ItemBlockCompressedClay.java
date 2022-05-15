package mods.clayium.block.itemblock;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

import mods.clayium.item.IClayEnergy;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemBlockCompressedClay
        extends ItemBlockDamaged
        implements IClayEnergy {
    public ItemBlockCompressedClay(Block block) {
        super(block);
    }


    public long getClayEnergy(ItemStack itemStack) {
        return (itemStack.getItemDamage() >= 4) ? (long) Math.pow(10.0D, (itemStack.getItemDamage() + 1)) : 0L;
    }


    public int getTier(ItemStack itemstack) {
        return itemstack.getItemDamage();
    }


    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean flag) {
        super.addInformation(itemstack, player, list, flag);
        list.add(UtilLocale.ClayEnergyNumeral(getClayEnergy(itemstack)) + "CE");
    }
}
