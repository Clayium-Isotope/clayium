package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

import mods.clayium.block.tile.TileClayMarker;
import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilBuilder;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ClayMarker
        extends BlockContainer implements ITieredBlock, ISpecialToolTip {
    public Block iconBlock;
    public Class<? extends TileEntity> tileEntityClass;
    public int tier;
    public int iconMetadata = -1;

    public ClayMarker(int tier, Block iconBlock, int iconMetadata, Class<? extends TileEntity> tileEntityClass) {
        super(Material.clay);
        setStepSound(soundTypeGravel);

        setHardness(0.5F);
        setResistance(5.0F);
        setHarvestLevel("shovel", 0);

        float f = 0.1875F;
        setBlockBounds(0.5F - f, 0.5F - f, 0.5F - f, 0.5F + f, 0.5F + f, 0.5F + f);

        this.iconBlock = iconBlock;
        this.tileEntityClass = tileEntityClass;
        this.tier = tier;
        this.iconMetadata = iconMetadata;
    }

    public ClayMarker(int tier, Block iconBlock, Class<? extends TileEntity> tileEntityClass) {
        this(tier, iconBlock, -1, tileEntityClass);
    }


    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
        setBlockBoundsBasedOnState((IBlockAccess) p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_);
        return super.getCollisionBoundingBoxFromPool(p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_);
    }


    public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_) {
        float f = 0.1875F;
        setBlockBounds(0.5F - f, 0.5F - f, 0.5F - f, 0.5F + f, 0.5F + f, 0.5F + f);
    }


    public void addCollisionBoxesToList(World p_149743_1_, int p_149743_2_, int p_149743_3_, int p_149743_4_, AxisAlignedBB p_149743_5_, List p_149743_6_, Entity p_149743_7_) {
        setBlockBoundsBasedOnState((IBlockAccess) p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_);
        super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_, p_149743_7_);
    }


    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        world.func_147453_f(x, y, z, block);
        super.breakBlock(world, x, y, z, block, meta);
    }


    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        try {
            return this.tileEntityClass.newInstance();
        } catch (InstantiationException e) {
            ClayiumCore.logger.catching(e);
            return null;
        } catch (IllegalAccessException e) {
            ClayiumCore.logger.catching(e);
            return null;
        }
    }


    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        TileClayMarker te = (TileClayMarker) UtilBuilder.safeGetTileEntity((IBlockAccess) world, x, y, z);
        te.activate();
        return true;
    }


    public boolean isOpaqueCube() {
        return false;
    }


    public boolean renderAsNormalBlock() {
        return false;
    }


    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
        return this.iconBlock.getIcon(p_149691_1_, (this.iconMetadata == -1) ? p_149691_2_ : this.iconMetadata);
    }


    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {}


    public int getTier(ItemStack itemstack) {
        return this.tier;
    }


    public int getTier(IBlockAccess world, int x, int y, int z) {
        return this.tier;
    }


    public List getTooltip(ItemStack itemStack) {
        return UtilLocale.localizeTooltip(getUnlocalizedName() + ".tooltip");
    }
}
