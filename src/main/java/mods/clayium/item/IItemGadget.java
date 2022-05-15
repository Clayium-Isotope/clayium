package mods.clayium.item;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IItemGadget {
    boolean match(ItemStack paramItemStack, World paramWorld, Entity paramEntity, int paramInt, boolean paramBoolean);

    void update(List<ItemStack> paramList, Entity paramEntity, boolean paramBoolean);
}
