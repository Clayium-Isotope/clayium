package mods.clayium.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

import mods.clayium.util.UtilLocale;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class ItemTiered
        extends Item
        implements ITieredItem {
    protected int baseTier = -1;


    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean flag) {
        if (getTier(itemstack) >= 0) {
            list.add(getTieredToolTip(getTier(itemstack)));
        }
        List<? extends String> alist = UtilLocale.localizeTooltip(getUnlocalizedName(itemstack) + ".tooltip");
        if (alist != null) {
            list.addAll(alist);
        }
    }


    public static String getTieredToolTip(int tier) {
        return EnumChatFormatting.WHITE + "Tier " + tier + EnumChatFormatting.RESET;
    }


    public EnumRarity getRarity(ItemStack itemstack) {
        switch (getTier(itemstack)) {
            case 4:
            case 5:
            case 6:
            case 7:
                return EnumRarity.uncommon;
            case 8:
            case 9:
            case 10:
            case 11:
                return EnumRarity.rare;
            case 12:
            case 13:
            case 14:
            case 15:
                return EnumRarity.epic;
        }


        return EnumRarity.common;
    }

    public ItemTiered setBaseTier(int tier) {
        this.baseTier = tier;
        return this;
    }

    public int getTier(ItemStack itemstack) {
        return this.baseTier;
    }
}
