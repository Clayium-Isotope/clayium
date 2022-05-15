package mods.clayium.util;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mods.clayium.core.ClayiumCore;
import mods.clayium.item.IAdvancedTool;
import mods.clayium.network.ClaySteelPickaxePacket;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;


public class UtilAdvancedTools {
    private static boolean firstCall = true;
    public static HashMap<EntityPlayer, Integer> sideList = new HashMap<EntityPlayer, Integer>();
    public static HashMap<EntityPlayer, HashMap<String, Integer>> forceList = new HashMap<EntityPlayer, HashMap<String, Integer>>();
    public static UtilAdvancedTools INSTANCE = new UtilAdvancedTools();


    public static int onBlockDestroyed(ItemStack itemstack, World world, Block block, int x, int y, int z, EntityLivingBase entity) {
        int damage = 0;
        if (!world.isRemote &&
                entity instanceof EntityPlayer) {
            for (Vec3 coord : getHarvestedCoordListInSafe(itemstack, world, x, y, z, (EntityPlayer) entity)) {
                if ((int) coord.xCoord == x && (int) coord.yCoord == y && (int) coord.zCoord == z) {
                    continue;
                }

                UtilBuilder.harvestBlockAndDropItems(world, (int) coord.xCoord, (int) coord.yCoord, (int) coord.zCoord, true, (world
                                .getBlock((int) coord.xCoord, (int) coord.yCoord, (int) coord.zCoord)
                                .canSilkHarvest(world, (EntityPlayer) entity, (int) coord.xCoord, (int) coord.yCoord, (int) coord.zCoord, world.getBlockMetadata((int) coord.xCoord, (int) coord.yCoord, (int) coord.zCoord)) &&
                                EnchantmentHelper.getSilkTouchModifier(entity)),
                        EnchantmentHelper.getFortuneModifier(entity));
                damage++;
            }
        }

        if (world.isRemote &&
                entity == UtilPlayer.getClientPlayer()) {
            ClayiumCore.packetDispatcher.sendToServer((IMessage) new ClaySteelPickaxePacket(world, x, y, z));
        }

        return damage;
    }


    @SubscribeEvent
    public void playerInteractEventSubscriber(PlayerInteractEvent event) {
        ItemStack item = event.entityPlayer.getCurrentEquippedItem();

        if (item == null || !(item.getItem() instanceof IAdvancedTool) || event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK || event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
            ;
    }


    public void playerTick(EntityPlayer player) {
        ClayiumCore.proxy.updateHittingSide(player);
    }


    @SubscribeEvent
    public void breakSpeedSubscriber(PlayerEvent.BreakSpeed event) {
        ItemStack item = event.entityPlayer.getCurrentEquippedItem();
        World world = event.entityPlayer.getEntityWorld();
        if (item != null && item.getItem() instanceof IAdvancedTool) {
            if (!world.isRemote && forceList.containsKey(event.entityPlayer)) {
                HashMap<String, Integer> map = forceList.get(event.entityPlayer);
                forceList.remove(event.entityPlayer);
                if (((Integer) map.get("x")).intValue() == event.x && ((Integer) map.get("y")).intValue() == event.y && ((Integer) map.get("z")).intValue() == event.z && ((Integer) map
                        .get("d")).intValue() == event.entityPlayer.worldObj.provider.dimensionId) {
                    event.newSpeed = Float.POSITIVE_INFINITY;


                    return;
                }
            }

            if (firstCall == true) {
                ClayiumCore.proxy.updateHittingSide(event.entityPlayer);
                firstCall = false;
                float hardness = 0.0F;

                for (Vec3 coord : getHarvestedCoordListInSafe(item, world, event.x, event.y, event.z, event.entityPlayer)) {
                    if (world.getBlock((int) coord.xCoord, (int) coord.yCoord, (int) coord.zCoord) != Blocks.air) {


                        float speed = world.getBlock((int) coord.xCoord, (int) coord.yCoord, (int) coord.zCoord).getPlayerRelativeBlockHardness(event.entityPlayer, world, event.x, event.y, event.z) * 30.0F;
                        hardness += (speed == 0.0F) ? Float.POSITIVE_INFINITY : (1.0F / speed);
                        if (world.getBlock((int) coord.xCoord, (int) coord.yCoord, (int) coord.zCoord) == Blocks.bedrock) {
                            hardness = Float.POSITIVE_INFINITY;
                        }
                    }
                }


                event.newSpeed = (hardness == 0.0F) ? Float.POSITIVE_INFINITY : (1.0F / hardness);


                if (event.newSpeed >= 24.0F &&
                        world.isRemote &&
                        event.entityPlayer == UtilPlayer.getClientPlayer()) {
                    ClayiumCore.packetDispatcher.sendToServer((IMessage) new ClaySteelPickaxePacket(event.entityPlayer.worldObj, event.x, event.y, event.z));
                }


                firstCall = true;
            }

            if (ClayiumCore.proxy.getHittingSide(event.entityPlayer) == -1) {
                event.newSpeed = 0.0F;
            }
        }
    }


    public static List<Vec3> getHarvestedCoordListInSafe(ItemStack itemstack, World world, int x, int y, int z, EntityPlayer player) {
        List<Vec3> list = new ArrayList<Vec3>();
        int side = ClayiumCore.proxy.getHittingSide(player);
        if (side == -1 || !(itemstack.getItem() instanceof IAdvancedTool)) {
            list.add(Vec3.createVectorHelper(x, y, z));
        } else {
            list = getHarvestedCoordList(itemstack, world, x, y, z, player, side);
        }
        return list;
    }


    public static List<Vec3> getHarvestedCoordList(ItemStack itemstack, World world, int x, int y, int z, EntityPlayer player, int side) {
        if (!(itemstack.getItem() instanceof IAdvancedTool)) {
            return null;
        }
        Vec3[] ev = getEigenVectors(player, side);
        return ((IAdvancedTool) itemstack.getItem()).getHarvestCoord().getHarvestedCoordList(itemstack, x, y, z, ev[0], ev[1], ev[2]);
    }

    public static Vec3[] getEigenVectorsInSafe(EntityPlayer player) {
        return (ClayiumCore.proxy.getHittingSide(player) == -1) ? null : getEigenVectors(player, ClayiumCore.proxy.getHittingSide(player));
    }


    public static Vec3[] getEigenVectors(EntityPlayer player, int side) {
        UtilDirection xxVector = UtilDirection.EAST;
        UtilDirection yyVector = UtilDirection.UP;
        UtilDirection zzVector = UtilDirection.SOUTH;
        UtilDirection clickedSide = UtilDirection.getOrientation(side);
        if (clickedSide != UtilDirection.UP && clickedSide != UtilDirection.DOWN) {
            zzVector = clickedSide;
            xxVector = zzVector.getSideOfDirection(UtilDirection.LEFTSIDE);
        } else {
            zzVector = UtilDirection.UP;
            float f1 = MathHelper.cos(-player.rotationYaw * 0.017453292F - 3.1415927F);
            float f2 = MathHelper.sin(-player.rotationYaw * 0.017453292F - 3.1415927F);
            if (f1 >= Math.sqrt(0.5D))
                yyVector = UtilDirection.NORTH;
            if (f1 <= -Math.sqrt(0.5D))
                yyVector = UtilDirection.SOUTH;
            if (f2 >= Math.sqrt(0.5D))
                yyVector = UtilDirection.WEST;
            if (f2 <= -Math.sqrt(0.5D))
                yyVector = UtilDirection.EAST;
            xxVector = yyVector.getSideOfDirection(UtilDirection.RIGHTSIDE);
            if (clickedSide == UtilDirection.DOWN) {
                yyVector = yyVector.getOpposite();
                zzVector = zzVector.getOpposite();
            }
        }
        return new Vec3[] {xxVector.toVec3(), yyVector.toVec3(), zzVector.toVec3()};
    }

    public static Vec3[] getInverse(Vec3[] v) {
        double d = (v[0]).xCoord * (v[1]).yCoord * (v[2]).zCoord + (v[1]).xCoord * (v[2]).yCoord * (v[0]).zCoord + (v[2]).xCoord * (v[0]).yCoord * (v[1]).zCoord - (v[0]).xCoord * (v[2]).yCoord * (v[1]).zCoord - (v[1]).xCoord * (v[0]).yCoord * (v[2]).zCoord - (v[2]).xCoord * (v[1]).yCoord * (v[0]).zCoord;


        if (d == 0.0D)
            return null;
        return new Vec3[] {
                Vec3.createVectorHelper(((v[1]).yCoord * (v[2]).zCoord - (v[2]).yCoord * (v[1]).zCoord) / d, ((v[2]).yCoord * (v[0]).zCoord - (v[0]).yCoord * (v[2]).zCoord) / d, ((v[0]).yCoord * (v[1]).zCoord - (v[1]).yCoord * (v[0]).zCoord) / d),


                Vec3.createVectorHelper(((v[1]).zCoord * (v[2]).xCoord - (v[2]).zCoord * (v[1]).xCoord) / d, ((v[2]).zCoord * (v[0]).xCoord - (v[0]).zCoord * (v[2]).xCoord) / d, ((v[0]).zCoord * (v[1]).xCoord - (v[1]).zCoord * (v[0]).xCoord) / d),


                Vec3.createVectorHelper(((v[1]).xCoord * (v[2]).yCoord - (v[2]).xCoord * (v[1]).yCoord) / d, ((v[2]).xCoord * (v[0]).yCoord - (v[0]).xCoord * (v[2]).yCoord) / d, ((v[0]).xCoord * (v[1]).yCoord - (v[1]).xCoord * (v[0]).yCoord) / d)
        };
    }
}


