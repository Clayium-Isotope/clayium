package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.clayium.block.tile.TileLaserReflector;
import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilDirection;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class LaserReflector
        extends ClayContainer implements ITieredBlock {
    public LaserReflector() {
        super(Material.iron, (Class) TileLaserReflector.class, 2);
        setStepSound(Block.soundTypeGlass);
        preventFirstPass();
    }


    public int getRenderType() {
        return ClayiumCore.laserReflectorRenderId;
    }


    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }


    protected boolean onBlockRightClicked(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        return false;
    }


    public int getTier(ItemStack itemstack) {
        return 7;
    }


    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        setSameIcons(par1IconRegister.registerIcon("clayium:laserreflector"));
    }


    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 1;
    }


    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        float f = 0.125F;
        switch (UtilDirection.getOrientation(world.getBlockMetadata(x, y, z))) {
            case UP:
            case DOWN:
                setBlockBounds(0.0F + f * 2.0F, 0.0F + f, 0.0F + f * 2.0F, 1.0F - f * 2.0F, 1.0F - f, 1.0F - f * 2.0F);
                return;
            case NORTH:
            case SOUTH:
                setBlockBounds(0.0F + f * 2.0F, 0.0F + f * 2.0F, 0.0F + f, 1.0F - f * 2.0F, 1.0F - f * 2.0F, 1.0F - f);
                return;
            case EAST:
            case WEST:
                setBlockBounds(0.0F + f, 0.0F + f * 2.0F, 0.0F + f * 2.0F, 1.0F - f, 1.0F - f * 2.0F, 1.0F - f * 2.0F);
                return;
        }
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }


    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        return getNormalSelectedBoundingBoxFromPool(world, x, y, z);
    }

    public int getTier(IBlockAccess world, int x, int y, int z) {
        return 7;
    }
}
