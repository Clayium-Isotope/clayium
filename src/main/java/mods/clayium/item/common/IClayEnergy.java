package mods.clayium.item.common;

import net.minecraft.item.ItemStack;

public interface IClayEnergy {
    long getClayEnergy();

    static long getClayEnergy(ItemStack itemstack) {
        if (!itemstack.isEmpty() && itemstack.getItem() instanceof IClayEnergy) {
            return ((IClayEnergy) itemstack.getItem()).getClayEnergy();
        }

        return 0L;
    }

    static boolean hasClayEnergy(ItemStack itemstack) {
        return getClayEnergy(itemstack) > 0L;
    }
}
