package mods.clayium.util;

import mods.clayium.machine.ClayContainer.ClayContainer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UtilBuilder {
    public static boolean rotateBlockByWrench(World world, BlockPos pos, EnumFacing side) {
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof ClayContainer) return false;

        EnumFacing direction = EnumFacing.getFront(side.ordinal());
        EnumFacing[] axes = block.getValidRotations(world, pos);
        if (axes == null || axes.length == 0) return false;

        EnumFacing axis = axes[0];
        for (EnumFacing axis1 : axes) {
            if (axis1 == direction)
                axis = axis1;
        }
        return block.rotateBlock(world, pos, axis);
    }

    public static ItemStack getItemBlock(World theWorld, BlockPos pos) {
//        ItemStack capsule = getFluidCapsule(theWorld, xx, yy, zz);
//        if (capsule != null) {
//            return capsule;
//        }

        IBlockState state = theWorld.getBlockState(pos);
        ItemStack silkitem = getRawItemBlock(state);
        NonNullList<ItemStack> dropitems = NonNullList.create();

        state.getBlock().getDrops(dropitems, theWorld, pos, state, 0);

        for (ItemStack dropitem : dropitems) {
            if (UtilItemStack.areItemDamageEqual(silkitem, dropitem)) {
                ItemStack res = dropitem.copy();
                res.setCount(1);
                return res;
            }
        }

        for (ItemStack dropitem : dropitems) {
            if (UtilItemStack.areItemEqual(silkitem, dropitem)) {
                ItemStack res = dropitem.copy();
                res.setCount(1);
                return res;
            }
        }

        return ItemStack.EMPTY;
    }

    public static ItemStack getRawItemBlock(IBlockState state) {
        int j = 0;
        Item item = Item.getItemFromBlock(state.getBlock());
        if (item.getHasSubtypes()) {
            j = state.getBlock().damageDropped(state);
        }

        return new ItemStack(item, 1, j);
    }
}
