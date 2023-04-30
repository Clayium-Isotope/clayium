package mods.clayium.item.common;

import mods.clayium.util.UsedFor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * The item which its class extends this is allowed to do special processing for Clay Container.
 */
@UsedFor(UsedFor.Type.Item)
public interface IModifyCC {
    EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ);

    default ItemStack getContainerItem(ItemStack itemStack) {
        ItemStack damaged = itemStack.copy();
        damaged.setItemDamage(damaged.getItemDamage() + 1);
        return damaged;
    }
}
