package mods.clayium.util;

import mods.clayium.core.ClayiumCore;
import mods.clayium.item.IAdvancedTool;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class UtilAdvancedTools {
    public static int onBlockDestroyed(ItemStack itemstack, World world, Block block, BlockPos pos, EntityLivingBase entity) {
        int damage = 0;
        if (!world.isRemote && entity instanceof EntityPlayer) {
            for (BlockPos coord : getHarvestedCoordListInSafe(itemstack, world, pos, (EntityPlayer) entity)) {
                world.destroyBlock(coord, true);
                damage++;
            }
        }

        return damage;
    }

    public static Vec3i[] getEigenVectors(EntityPlayer player, EnumFacing facing) {
        EnumFacing xxVector = EnumFacing.EAST;
        EnumFacing yyVector = EnumFacing.UP;
        EnumFacing zzVector = EnumFacing.SOUTH;

        if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {

            zzVector = EnumFacing.UP;
            float f1 = MathHelper.cos((float) (Math.toRadians(-player.rotationYaw) - Math.PI));
            float f2 = MathHelper.sin((float) (Math.toRadians(-player.rotationYaw) - Math.PI));
            if (f1 >= Math.sqrt(0.5D)) yyVector = EnumFacing.NORTH;
            if (f1 <= -Math.sqrt(0.5D)) yyVector = EnumFacing.SOUTH;
            if (f2 >= Math.sqrt(0.5D)) yyVector = EnumFacing.WEST;
            if (f2 <= -Math.sqrt(0.5D)) yyVector = EnumFacing.EAST;
            xxVector = yyVector.rotateYCCW();

            if (facing == EnumFacing.DOWN) {
                yyVector = yyVector.getOpposite();
                zzVector = zzVector.getOpposite();
            }
        } else {
            zzVector = facing;
            xxVector = zzVector.rotateY();
        }

        return new Vec3i[] {xxVector.getDirectionVec(), yyVector.getDirectionVec(), zzVector.getDirectionVec()};
    }

    public static List<BlockPos> getHarvestedCoordListInSafe(ItemStack itemstack, World world, BlockPos pos, EntityPlayer player) {
        List<BlockPos> list = new ArrayList<>();

        NBTTagCompound compound = itemstack.getTagCompound();
        if (compound == null) {
            return list;
        }

        EnumFacing facing = ClayiumCore.proxy.getHittingSide(player);
        if (!(itemstack.getItem() instanceof IAdvancedTool)) {
            list.add(pos);
        } else {
            list = getHarvestedCoordList(itemstack, world, pos, player, facing);
        }
        return list;
    }

    public static List<BlockPos> getHarvestedCoordList(ItemStack itemstack, World world, BlockPos pos, EntityPlayer player, EnumFacing facing) {
        if (!(itemstack.getItem() instanceof IAdvancedTool)) {
            return new ArrayList<>();
        }

        RayTraceResult rtr = world.rayTraceBlocks(player.getPositionEyes(3.0F), player.getLookVec().scale(9999.0D), false, false, true);
        if (rtr == null) {
            return new ArrayList<>();
        }

        Vec3i[] ev = getEigenVectors(player, rtr.sideHit);
        return ((IAdvancedTool) itemstack.getItem()).getHarvestCoord().getHarvestedCoordList(itemstack, pos, ev[0], ev[1], ev[2]);
    }
}
