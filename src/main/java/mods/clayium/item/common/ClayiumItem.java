package mods.clayium.item.common;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilLocale;

public class ClayiumItem extends Item {

    public ClayiumItem(String modelPath) {
        setTranslationKey(modelPath);
        setRegistryName(ClayiumCore.ModId, modelPath);
        setCreativeTab(ClayiumCore.tabClayium);
    }

    public ClayiumItem(ClayiumMaterial material, ClayiumShape shape) {
        this(material.getName() + "_" + shape.getName());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        UtilLocale.localizeTooltip(tooltip, getTranslationKey());
    }
}
