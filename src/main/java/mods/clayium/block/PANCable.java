package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

import mods.clayium.core.ClayiumCore;
import mods.clayium.pan.UtilPAN;
import mods.clayium.util.UtilDirection;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class PANCable
        extends Block implements IPANConductor, ITieredBlock {
    public PANCable() {
        super(Material.glass);
        setStepSound(soundTypeGlass);
        setHardness(0.2F).setResistance(0.2F);
    }


    public int getRenderType() {
        return ClayiumCore.panCableRenderId;
    }


    public boolean renderAsNormalBlock() {
        return false;
    }


    public boolean isOpaqueCube() {
        return false;
    }


    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 1;
    }


    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        this.blockIcon = p_149651_1_.registerIcon("clayium:pancable");
    }

    public static float pipeWidth = 0.125F;
    private static UtilDirection tracingDirection = null;


    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 start, Vec3 end) {
        UtilDirection mindirection = null;
        MovingObjectPosition res = null;
        double o = pipeWidth;

        setBlockBounds((float) (0.5D - o), (float) (0.5D - o), (float) (0.5D - o), (float) (0.5D + o), (float) (0.5D + o), (float) (0.5D + o));
        res = super.collisionRayTrace(world, x, y, z, start, end);


        UtilDirection[] directions = {UtilDirection.NORTH, UtilDirection.SOUTH, UtilDirection.EAST, UtilDirection.WEST, UtilDirection.UP, UtilDirection.DOWN};
        for (UtilDirection direction : directions) {
            if (checkPipeConnection(world, x, y, z, direction)) {
                setBlockBounds((float) (0.5D - o + ((direction.offsetX == 1) ? (o * 2.0D) : 0.0D) - ((direction.offsetX == -1) ? (0.5D - o) : 0.0D)), (float) (0.5D - o + ((direction.offsetY == 1) ? (o * 2.0D) : 0.0D) - ((direction.offsetY == -1) ? (0.5D - o) : 0.0D)), (float) (0.5D - o + ((direction.offsetZ == 1) ? (o * 2.0D) : 0.0D) - ((direction.offsetZ == -1) ? (0.5D - o) : 0.0D)), (float) (0.5D + o - ((direction.offsetX == -1) ? (o * 2.0D) : 0.0D) + ((direction.offsetX == 1) ? (0.5D - o) : 0.0D)), (float) (0.5D + o - ((direction.offsetY == -1) ? (o * 2.0D) : 0.0D) + ((direction.offsetY == 1) ? (0.5D - o) : 0.0D)), (float) (0.5D + o - ((direction.offsetZ == -1) ? (o * 2.0D) : 0.0D) + ((direction.offsetZ == 1) ? (0.5D - o) : 0.0D)));


                MovingObjectPosition pos = super.collisionRayTrace(world, x, y, z, start, end);
                if (pos != null && (
                        res == null || pos.hitVec.distanceTo(start) < res.hitVec.distanceTo(start))) {

                    res = pos;
                    mindirection = direction;
                }
            }
        }

        setBlockBounds((float) (0.5D - o), (float) (0.5D - o), (float) (0.5D - o), (float) (0.5D + o), (float) (0.5D + o), (float) (0.5D + o));
        tracingDirection = mindirection;
        return res;
    }


    public boolean checkPipeConnection(IBlockAccess world, int x, int y, int z, UtilDirection direction) {
        return UtilPAN.isPANConductor(world, x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
    }


    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        double o = pipeWidth;
        if (tracingDirection == null) {
            setBlockBounds((float) (0.5D - o), (float) (0.5D - o), (float) (0.5D - o), (float) (0.5D + o), (float) (0.5D + o), (float) (0.5D + o));
        } else {
            UtilDirection direction = tracingDirection;
            setBlockBounds((float) (0.5D - o + ((direction.offsetX == 1) ? (o * 2.0D) : 0.0D) - ((direction.offsetX == -1) ? (0.5D - o) : 0.0D)), (float) (0.5D - o + ((direction.offsetY == 1) ? (o * 2.0D) : 0.0D) - ((direction.offsetY == -1) ? (0.5D - o) : 0.0D)), (float) (0.5D - o + ((direction.offsetZ == 1) ? (o * 2.0D) : 0.0D) - ((direction.offsetZ == -1) ? (0.5D - o) : 0.0D)), (float) (0.5D + o - ((direction.offsetX == -1) ? (o * 2.0D) : 0.0D) + ((direction.offsetX == 1) ? (0.5D - o) : 0.0D)), (float) (0.5D + o - ((direction.offsetY == -1) ? (o * 2.0D) : 0.0D) + ((direction.offsetY == 1) ? (0.5D - o) : 0.0D)), (float) (0.5D + o - ((direction.offsetZ == -1) ? (o * 2.0D) : 0.0D) + ((direction.offsetZ == 1) ? (0.5D - o) : 0.0D)));
        }


        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }


    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getNormalSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }


    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
        setBlockBoundsBasedOnState(p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_);
        return super.getCollisionBoundingBoxFromPool(p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_);
    }


    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity p_149743_7_) {
        double o = pipeWidth;
        AxisAlignedBB axisalignedbb1 = AxisAlignedBB.getBoundingBox(x + 0.5D - o, y + 0.5D - o, z + 0.5D - o, x + 0.5D + o, y + 0.5D + o, z + 0.5D + o);
        if (axisalignedbb1 != null && aabb.intersectsWith(axisalignedbb1)) {
            list.add(axisalignedbb1);
        }
        UtilDirection[] directions = {UtilDirection.NORTH, UtilDirection.SOUTH, UtilDirection.EAST, UtilDirection.WEST, UtilDirection.UP, UtilDirection.DOWN};
        for (UtilDirection direction : directions) {
            if (checkPipeConnection(world, x, y, z, direction)) {
                axisalignedbb1 = AxisAlignedBB.getBoundingBox(x + 0.5D - o + ((direction.offsetX == 1) ? (o * 2.0D) : 0.0D) - ((direction.offsetX == -1) ? (0.5D - o) : 0.0D), y + 0.5D - o + ((direction.offsetY == 1) ? (o * 2.0D) : 0.0D) - ((direction.offsetY == -1) ? (0.5D - o) : 0.0D), z + 0.5D - o + ((direction.offsetZ == 1) ? (o * 2.0D) : 0.0D) - ((direction.offsetZ == -1) ? (0.5D - o) : 0.0D), x + 0.5D + o - ((direction.offsetX == -1) ? (o * 2.0D) : 0.0D) + ((direction.offsetX == 1) ? (0.5D - o) : 0.0D), y + 0.5D + o - ((direction.offsetY == -1) ? (o * 2.0D) : 0.0D) + ((direction.offsetY == 1) ? (0.5D - o) : 0.0D), z + 0.5D + o - ((direction.offsetZ == -1) ? (o * 2.0D) : 0.0D) + ((direction.offsetZ == 1) ? (0.5D - o) : 0.0D));


                if (axisalignedbb1 != null && aabb.intersectsWith(axisalignedbb1)) {
                    list.add(axisalignedbb1);
                }
            }
        }
    }


    public int getTier(ItemStack itemstack) {
        return 11;
    }


    public int getTier(IBlockAccess world, int x, int y, int z) {
        return 11;
    }
}
