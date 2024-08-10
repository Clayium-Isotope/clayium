package mods.clayium.util;

import mods.clayium.machine.ClayContainer.ClayContainer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UtilBuilder {

    public static boolean rotateBlockByWrench(World world, BlockPos pos, EnumFacing side) {
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof ClayContainer) return false;

        EnumFacing direction = EnumFacing.byIndex(side.ordinal());
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
        // ItemStack capsule = getFluidCapsule(theWorld, xx, yy, zz);
        // if (capsule != null) {
        // return capsule;
        // }

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

    public static ItemStack getPickedBlock(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        RayTraceResult rtr = new RayTraceResult(RayTraceResult.Type.BLOCK, Vec3d.ZERO, EnumFacing.NORTH, pos);

        return state.getBlock().getPickBlock(state, rtr, world, pos, UtilPlayer.getDefaultFake());
    }

    public static ItemStack getRawItemBlock(IBlockState state) {
        int j = 0;
        Item item = Item.getItemFromBlock(state.getBlock());
        if (item.getHasSubtypes()) {
            j = state.getBlock().damageDropped(state);
        }

        return new ItemStack(item, 1, j);
    }

    public static void dropItems(World theWorld, BlockPos pos, List<ItemStack> itemToDrop) {
        if (theWorld.isRemote || !theWorld.getGameRules().getBoolean("doTileDrops") || theWorld.restoringBlockSnapshots) return;
        final float f = 0.7F;
        final double center = (1.0F - f) * 0.5d;

        for (ItemStack item : itemToDrop) {
            double d0 = theWorld.rand.nextFloat() * f;
            double d1 = theWorld.rand.nextFloat() * f;
            double d2 = theWorld.rand.nextFloat() * f;
            EntityItem entityitem = new EntityItem(theWorld, (double) pos.getX() + d0 + center, (double) pos.getY() + d1 + center, (double) pos.getZ() + d2 + center, item);
            entityitem.setPickupDelay(10);
            theWorld.spawnEntity(entityitem);
        }
    }

    /**
     * If player is known, use {@link Block#harvestBlock(World, EntityPlayer, BlockPos, IBlockState, TileEntity, ItemStack)}
     */
    public static List<ItemStack> harvestBlock(World theWorld, BlockPos pos, boolean dropXP, boolean doSilkHarvest, int fortune, boolean collectFluid) {

        List<ItemStack> itemToDrop = getDropsFromBlock(theWorld, pos, doSilkHarvest, fortune, collectFluid);
        destroyBlock(theWorld, pos, dropXP, doSilkHarvest, fortune);
        return itemToDrop;
    }

    public static List<ItemStack> getDropsFromBlock(World world, BlockPos pos, boolean canSilkHarvest, int fortune, boolean collectFluid) {
        if (world.isRemote || world.restoringBlockSnapshots) {
            return Collections.emptyList();
        }

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == Blocks.AIR) {
            return Collections.emptyList();
        }

        if (collectFluid) {
            ItemStack itemstack = getFluidCapsule(world, pos);
            if (!itemstack.isEmpty()) {
                return Collections.singletonList(itemstack);
            }
        }

        if (canSilkHarvest && state.getBlock().canSilkHarvest(world, pos, state, UtilPlayer.getDefaultFake())) {
            ItemStack itemstack = getPickedBlock(world, pos);
            if (!itemstack.isEmpty()) {
                return Collections.singletonList(itemstack);
            }
            return Collections.emptyList();
        }

        NonNullList<ItemStack> items = NonNullList.create();
        state.getBlock().getDrops(items, world, pos, state, fortune);
        final float g = ForgeEventFactory.fireBlockHarvesting(items, world, pos, state, fortune, 1.0f, canSilkHarvest, UtilPlayer.getDefaultFake());

        return items.stream().filter(stack -> world.rand.nextFloat() <= g).collect(Collectors.toList());
    }

    public static List<ItemStack> getDropsFromBlock(World theWorld, BlockPos pos, boolean canSilkHarvest, int fortune) {
        return getDropsFromBlock(theWorld, pos, canSilkHarvest, fortune, true);
    }

    public static ItemStack getFluidCapsule(World world, BlockPos pos) {
        return ItemStack.EMPTY;
//        return ItemCapsule.getItemCapsuleFromFluidStack(getFluidStack(theWorld, xx, yy, zz));
    }

//    public static FluidStack getFluidStack(World theWorld, int xx, int yy, int zz) {
//        Block block = theWorld.getBlock(xx, yy, zz);
//        if (block != Blocks.water && block != Blocks.flowing_water) {
//            if (block != Blocks.lava && block != Blocks.flowing_lava) {
//                Fluid fluid = FluidRegistry.lookupFluidForBlock(block);
//                if (fluid != null) {
//                    if (block instanceof IFluidBlock) {
//                        return !((IFluidBlock)block).canDrain(theWorld, xx, yy, zz) ? null : ((IFluidBlock)block).drain(theWorld, xx, yy, zz, false);
//                    } else {
//                        return theWorld.getBlockMetadata(xx, yy, zz) == 0 ? new FluidStack(fluid, 1000) : null;
//                    }
//                } else {
//                    return null;
//                }
//            } else {
//                return theWorld.getBlockMetadata(xx, yy, zz) == 0 ? new FluidStack(FluidRegistry.LAVA, 1000) : null;
//            }
//        } else {
//            return theWorld.getBlockMetadata(xx, yy, zz) == 0 ? new FluidStack(FluidRegistry.WATER, 1000) : null;
//        }
//    }

    public static void destroyBlock(World world, BlockPos pos, boolean dropXP, boolean canSilkHarvest, int fortune) {
        IBlockState state = world.getBlockState(pos);
        if (!world.isRemote) {
            world.playSound(null, pos, state.getBlock().getSoundType(state, world, pos, null).getBreakSound(), SoundCategory.BLOCKS, 1.0f, 1.0f);
        }

        world.setBlockToAir(pos);
        if (dropXP) {
            state.getBlock().dropXpOnBlockBreak(world, pos, state.getBlock().getExpDrop(state, world, pos, fortune));
        }
    }

    public static void harvestAndDropItself(World theWorld, BlockPos pos, boolean dropXP, boolean canSilkHarvest, int fortune, boolean collectFluid) {
        dropItems(theWorld, pos, harvestBlock(theWorld, pos, dropXP, canSilkHarvest, fortune, collectFluid));
    }

    public static List<Vec3d> getCorners(AxisAlignedBB aabb) {
        return Arrays.asList(
                new Vec3d(aabb.minX, aabb.minY, aabb.minZ), new Vec3d(aabb.minX, aabb.minY, aabb.maxZ),
                new Vec3d(aabb.minX, aabb.maxY, aabb.minZ), new Vec3d(aabb.minX, aabb.maxY, aabb.maxZ),
                new Vec3d(aabb.maxX, aabb.minY, aabb.minZ), new Vec3d(aabb.maxX, aabb.minY, aabb.maxZ),
                new Vec3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vec3d(aabb.maxX, aabb.maxY, aabb.maxZ)
                );
    }

    public static boolean placeBlockByItemBlock(ItemStack itemStack, World world, BlockPos pos) {
        if (itemStack.isEmpty() || !(itemStack.getItem() instanceof ItemBlock)) {
            return false;
        }

        return world.setBlockState(pos, ((ItemBlock) itemStack.getItem()).getBlock().getDefaultState());
//        return placeBlockByItemBlock(itemStack, world, pos, EnumFacing.UP, 0.5f, 0.5f, 0.5f) == EnumActionResult.SUCCESS;
    }

    public static EnumActionResult placeBlockByItemBlock(ItemStack itemstack, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (itemstack.isEmpty() || !(itemstack.getItem() instanceof ItemBlock)) {
            return EnumActionResult.FAIL;
        }

        return ForgeHooks.onPlaceItemIntoWorld(itemstack, UtilPlayer.getDefaultFake(), world, pos, side, hitX, hitY, hitZ, EnumHand.MAIN_HAND);
    }
}
