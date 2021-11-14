package mods.clayium.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import mods.clayium.core.ClayiumCore;
import mods.clayium.item.ItemCapsule;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;


public class UtilBuilder {
    public static ItemStack[] harvestBlock(World theWorld, int xx, int yy, int zz, boolean dropXP, boolean canSilkHarvest, int fortune, boolean collectFluid) {
        Block block = theWorld.getBlock(xx, yy, zz);
        int l = theWorld.getBlockMetadata(xx, yy, zz);
        ItemStack[] itemToDrop = getDropsFromBlock(theWorld, xx, yy, zz, canSilkHarvest, fortune, collectFluid);
        destroyBlock(theWorld, xx, yy, zz, dropXP, canSilkHarvest, fortune);


        return itemToDrop;
    }


    public static ItemStack[] harvestBlock(World theWorld, int xx, int yy, int zz, boolean dropXP, boolean canSilkHarvest, int fortune) {
        return harvestBlock(theWorld, xx, yy, zz, dropXP, canSilkHarvest, fortune, true);
    }


    public static void destroyBlock(World theWorld, int xx, int yy, int zz, boolean dropXP, boolean canSilkHarvest, int fortune) {
        Block block = theWorld.getBlock(xx, yy, zz);
        int l = theWorld.getBlockMetadata(xx, yy, zz);

        if (!theWorld.isRemote)
            theWorld.playAuxSFXAtEntity(null, 2001, xx, yy, zz, Block.getIdFromBlock(block) + (theWorld.getBlockMetadata(xx, yy, zz) << 12));
        theWorld.setBlockToAir(xx, yy, zz);

        if (dropXP)
            block.dropXpOnBlockBreak(theWorld, xx, yy, zz, block.getExpDrop((IBlockAccess) theWorld, l, fortune));
    }

    public static ItemStack getFluidCapsule(World theWorld, int xx, int yy, int zz) {
        return ItemCapsule.getItemCapsuleFromFluidStack(getFluidStack(theWorld, xx, yy, zz));
    }

    public static FluidStack getFluidStack(World theWorld, int xx, int yy, int zz) {
        Block block = theWorld.getBlock(xx, yy, zz);
        if (block == Blocks.water || block == Blocks.flowing_water)
            return (theWorld.getBlockMetadata(xx, yy, zz) == 0) ? new FluidStack(FluidRegistry.WATER, 1000) : null;
        if (block == Blocks.lava || block == Blocks.flowing_lava) {
            return (theWorld.getBlockMetadata(xx, yy, zz) == 0) ? new FluidStack(FluidRegistry.LAVA, 1000) : null;
        }
        Fluid fluid = FluidRegistry.lookupFluidForBlock(block);
        if (fluid != null) {
            if (block instanceof IFluidBlock) {
                if (!((IFluidBlock) block).canDrain(theWorld, xx, yy, zz))
                    return null;
                return ((IFluidBlock) block).drain(theWorld, xx, yy, zz, false);
            }
            return (theWorld.getBlockMetadata(xx, yy, zz) == 0) ? new FluidStack(fluid, 1000) : null;
        }

        return null;
    }


    private static Method createStackedBlock = null;

    public static ItemStack createStackedBlock(Block block, int metadata) {
        if (createStackedBlock == null) {
            Class<?> classBlock = null;
            try {
                classBlock = Class.forName("net.minecraft.block.Block");
            } catch (ClassNotFoundException e2) {
                try {
                    classBlock = Class.forName("net.minecraft.aji");
                } catch (ClassNotFoundException e) {
                    ClayiumCore.logger.catching(e);
                }
            }
            if (classBlock != null) {
                try {
                    createStackedBlock = classBlock.getDeclaredMethod("createStackedBlock", new Class[] {int.class});
                } catch (NoSuchMethodException e) {
                    try {
                        createStackedBlock = classBlock.getDeclaredMethod("func_149644_j", new Class[] {int.class});
                    } catch (NoSuchMethodException e1) {
                        try {
                            createStackedBlock = classBlock.getDeclaredMethod("j", new Class[] {int.class});
                        } catch (NoSuchMethodException e2) {
                            ClayiumCore.logger.catching(e2);
                        } catch (SecurityException e2) {
                            ClayiumCore.logger.catching(e2);
                        }
                    } catch (SecurityException e1) {
                        ClayiumCore.logger.catching(e1);
                    }
                } catch (SecurityException e) {
                    ClayiumCore.logger.catching(e);
                }
            }
            if (createStackedBlock != null) {
                createStackedBlock.setAccessible(true);
            }
        }
        ItemStack itemstack = null;
        if (createStackedBlock != null) {
            Object obj = null;
            try {
                obj = createStackedBlock.invoke(block, new Object[] {Integer.valueOf(metadata)});
            } catch (Exception e) {
                ClayiumCore.logger.catching(e);
            }
            if (obj instanceof ItemStack)
                itemstack = (ItemStack) obj;
        }
        return itemstack;
    }

    public static ItemStack[] getDropsFromBlock(World theWorld, int xx, int yy, int zz, boolean canSilkHarvest, int fortune, boolean collectFluid) {
        Block block = theWorld.getBlock(xx, yy, zz);
        if (block == Blocks.air)
            return new ItemStack[0];
        int l = theWorld.getBlockMetadata(xx, yy, zz);
        List<ItemStack> itemToDrop = new ArrayList<ItemStack>();
        if (collectFluid) {
            ItemStack fluidCapsule = getFluidCapsule(theWorld, xx, yy, zz);
            if (fluidCapsule != null) {
                itemToDrop.add(fluidCapsule);
                return itemToDrop.<ItemStack>toArray(new ItemStack[0]);
            }
        }
        if (canSilkHarvest && block.canSilkHarvest(theWorld, UtilPlayer.getFakePlayer(null), xx, yy, zz, l)) {


            ItemStack itemstack = createStackedBlock(block, l);


            if (itemstack != null) {
                itemToDrop.add(itemstack);
            }
        } else {

            float g = 1.0F;
            if (!theWorld.isRemote && !theWorld.restoringBlockSnapshots) {

                ArrayList<ItemStack> items = block.getDrops(theWorld, xx, yy, zz, l, fortune);
                g = ForgeEventFactory.fireBlockHarvesting(items, theWorld, block, xx, yy, zz, l, fortune, g, false, null);

                for (ItemStack item : items) {

                    if (theWorld.rand.nextFloat() <= g) {


                        itemToDrop.add(item);
                    }
                }
            }
        }
        return itemToDrop.<ItemStack>toArray(new ItemStack[0]);
    }

    public static ItemStack[] getDropsFromBlock(World theWorld, int xx, int yy, int zz, boolean canSilkHarvest, int fortune) {
        return getDropsFromBlock(theWorld, xx, yy, zz, canSilkHarvest, fortune, true);
    }


    public static ItemStack getItemBlock(World theWorld, int xx, int yy, int zz) {
        ItemStack capsule = getFluidCapsule(theWorld, xx, yy, zz);
        if (capsule != null) {
            return capsule;
        }
        ItemStack silkitem = getRawItemBlock(theWorld, xx, yy, zz);
        ArrayList<ItemStack> dropitems = theWorld.getBlock(xx, yy, zz).getDrops(theWorld, xx, yy, zz, theWorld.getBlockMetadata(xx, yy, zz), 0);

        for (ItemStack dropitem : dropitems) {
            if (UtilItemStack.areItemDamageEqual(silkitem, dropitem)) {
                ItemStack res = dropitem.copy();
                res.stackSize = 1;
                return res;
            }
        }
        for (ItemStack dropitem : dropitems) {
            if (UtilItemStack.areItemEqual(silkitem, dropitem)) {
                ItemStack res = dropitem.copy();
                res.stackSize = 1;
                return res;
            }
        }
        return silkitem;
    }

    public static ItemStack getRawItemBlock(World theWorld, int xx, int yy, int zz) {
        Block block = theWorld.getBlock(xx, yy, zz);


        int j = 0;
        Item item = Item.getItemFromBlock(block);
        if (item != null && item.getHasSubtypes()) {

            j = block.getDamageValue(theWorld, xx, yy, zz);
        }
        return new ItemStack(item, 1, j);
    }


    public static void dropItems(World theWorld, int xx, int yy, int zz, ItemStack[] itemToDrop) {
        for (ItemStack item : itemToDrop) {
            if (!theWorld.isRemote && theWorld.getGameRules().getGameRuleBooleanValue("doTileDrops") && !theWorld.restoringBlockSnapshots) {

                float f = 0.7F;
                double d0 = (theWorld.rand.nextFloat() * f) + (1.0F - f) * 0.5D;
                double d1 = (theWorld.rand.nextFloat() * f) + (1.0F - f) * 0.5D;
                double d2 = (theWorld.rand.nextFloat() * f) + (1.0F - f) * 0.5D;
                EntityItem entityitem = new EntityItem(theWorld, xx + d0, yy + d1, zz + d2, item);
                entityitem.delayBeforeCanPickup = 10;
                theWorld.spawnEntityInWorld((Entity) entityitem);
            }
        }
    }

    public static void harvestBlockAndDropItems(World theWorld, int xx, int yy, int zz, boolean dropXP, boolean canSilkHarvest, int fortune, boolean collectFluid) {
        dropItems(theWorld, xx, yy, zz, harvestBlock(theWorld, xx, yy, zz, dropXP, canSilkHarvest, fortune, collectFluid));
    }

    public static void harvestBlockAndDropItems(World theWorld, int xx, int yy, int zz, boolean dropXP, boolean canSilkHarvest, int fortune) {
        harvestBlockAndDropItems(theWorld, xx, yy, zz, dropXP, canSilkHarvest, fortune, true);
    }


    public static boolean placeBlockByRightClick(Block block, int meta, ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int num) {
        int[] coord = coordTransformOnPlaceBlock(world, x, y, z, side);
        x = coord[0];
        y = coord[1];
        z = coord[2];
        side = coord[3];

        if (itemstack.stackSize == 0) {
            return false;
        }
        if (!player.canPlayerEdit(x, y, z, side, itemstack)) {
            return false;
        }
        if (y == 255 && block.getMaterial().isSolid()) {
            return false;
        }
        if (world.canPlaceEntityOnSide(block, x, y, z, false, side, (Entity) player, itemstack)) {

            int j1 = block.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, meta);

            if (placeBlockByPlayer(block, itemstack, player, world, x, y, z, side, hitX, hitY, hitZ, j1)) {
                postBlockPlace(block, world, itemstack, x, y, z, num);
            }

            return true;
        }


        return false;
    }


    public static int[] coordTransformOnPlaceBlock(World world, int x, int y, int z, int side) {
        Block b = world.getBlock(x, y, z);

        if (b == Blocks.snow_layer && (world.getBlockMetadata(x, y, z) & 0x7) < 1) {

            side = 1;
        } else if (b != Blocks.vine && b != Blocks.tallgrass && b != Blocks.deadbush && !b.isReplaceable((IBlockAccess) world, x, y, z)) {

            if (side == 0) {
                y--;
            }

            if (side == 1) {
                y++;
            }

            if (side == 2) {
                z--;
            }

            if (side == 3) {
                z++;
            }

            if (side == 4) {
                x--;
            }

            if (side == 5) {
                x++;
            }
        }
        return new int[] {x, y, z, side};
    }


    public static boolean placeBlockByPlayer(Block block, ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        if (!world.setBlock(x, y, z, block, metadata, 3)) {
            return false;
        }

        if (world.getBlock(x, y, z) == block) {

            block.onBlockPlacedBy(world, x, y, z, (EntityLivingBase) player, stack);
            block.onPostBlockPlaced(world, x, y, z, metadata);
        }

        return true;
    }


    public static boolean placeBlockByItemBlock(ItemStack itemstack, World world, int x, int y, int z) {
        return placeBlockByItemBlock(itemstack, world, x, y, z, 1, 0.5F, 0.5F, 0.5F);
    }


    public static boolean placeBlockByItemBlock(ItemStack itemstack, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (itemstack == null || !(itemstack.getItem() instanceof ItemBlock)) {
            return false;
        }
        ItemBlock itemBlock = (ItemBlock) itemstack.getItem();

        int[] coord = coordTransformOnPlaceBlock(world, x, y, z, side);
        x = coord[0];
        y = coord[1];
        z = coord[2];
        side = coord[3];

        if (itemstack.stackSize == 0)
            return false;
        if (y == 255 && itemBlock.field_150939_a.getMaterial().isSolid()) {
            return false;
        }
        int i1 = itemBlock.getMetadata(itemstack.getItemDamage());
        int j1 = itemBlock.field_150939_a.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, i1);

        if (placeBlockAt(itemBlock.field_150939_a, itemstack, world, x, y, z, j1, true)) {
            postBlockPlace(itemBlock.field_150939_a, world, itemstack, x, y, z, 1);
        }
        return true;
    }


    public static boolean placeBlockAt(Block block, ItemStack stack, World world, int x, int y, int z, int metadata, boolean useFakePlayer) {
        if (!world.setBlock(x, y, z, block, metadata, 3)) {
            return false;
        }

        if (world.getBlock(x, y, z) == block) {

            if (useFakePlayer) {
                block.onBlockPlacedBy(world, x, y, z, (EntityLivingBase) UtilPlayer.getFakePlayer((String) null, world, x, y, z), stack);
            }
            block.onPostBlockPlaced(world, x, y, z, metadata);
        }

        return true;
    }


    public static boolean placeBlockAt(Block block, World world, int x, int y, int z, int metadata) {
        return placeBlockAt(block, null, world, x, y, z, metadata, false);
    }


    public static boolean rotateBlockByWrench(World world, int x, int y, int z, int side) {
        Block block = world.getBlock(x, y, z);
        if (block instanceof mods.clayium.block.ClayContainer)
            return false;
        ForgeDirection direction = ForgeDirection.getOrientation(side);
        ForgeDirection[] axes = block.getValidRotations(world, x, y, z);
        if (axes == null || axes.length == 0)
            return false;
        ForgeDirection axis = axes[0];
        for (ForgeDirection axis1 : block.getValidRotations(world, x, y, z)) {
            if (axis1 == direction)
                axis = axis1;
        }
        return block.rotateBlock(world, x, y, z, axis);
    }

    public static void postBlockPlace(Block block, World world, ItemStack item, int x, int y, int z, int num) {
        world.playSoundEffect((x + 0.5F), (y + 0.5F), (z + 0.5F), block.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
        item.stackSize -= num;
    }


    public static TileEntity safeGetTileEntity(IBlockAccess world, int x, int y, int z) {
        return (y >= 0 && y < 256 && x >= -30000000 && z >= -30000000 && x < 30000000 && z < 30000000) ? world.getTileEntity(x, y, z) : null;
    }
}


