package mods.clayium.item.common;

import mods.clayium.block.ClayiumBlocks;
import mods.clayium.block.itemblock.ItemBlockCompressedClay;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
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

    /**
     * Get tier of the stack as if the item is Clay or Compressed Clay
     */
    static int getTier(ItemStack stack) {
        if (stack.isEmpty()) return -1;

        if (stack.getItem().equals(Item.getItemFromBlock(Blocks.CLAY)))
            return 0;

        if (stack.getItem() instanceof ItemBlockCompressedClay) {
            return ((ItemBlockCompressedClay) stack.getItem()).getTier();
        }

        return -1;
    }

    /**
     * @return ItemStack::new で返しているので、たぶん {@code ItemStack.copy()} は不要
     */
    static ItemStack getCompressedClay(int tier, int size) {
        return tier <= 0 ? new ItemStack(Blocks.CLAY, size) : ClayiumBlocks.compressedClay.get(tier - 1, size);
    }

    static ItemStack getCompressedClay(int tier) {
        return getCompressedClay(tier, 1);
    }
}
