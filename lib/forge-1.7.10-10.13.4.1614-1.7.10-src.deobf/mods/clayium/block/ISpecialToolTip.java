package mods.clayium.block;

import java.util.List;

import net.minecraft.item.ItemStack;

public interface ISpecialToolTip {
    List getTooltip(ItemStack paramItemStack);
}
