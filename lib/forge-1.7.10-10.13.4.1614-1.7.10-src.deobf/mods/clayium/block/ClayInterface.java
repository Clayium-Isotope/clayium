package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

import mods.clayium.block.tile.TileClayContainerInterface;
import mods.clayium.util.UtilBuilder;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class ClayInterface
        extends ClayContainerTiered {
    protected String iconstr;
    static int nestCounter = 0;
    protected IIcon DefaultInsertIcon;
    protected IIcon DefaultExtractIcon;

    public ClayInterface(int tier) {
        this("clayium:interface", TileClayContainerInterface.class, tier);
    }

    protected IIcon DefaultInsertPipeIcon;
    protected IIcon DefaultExtractPipeIcon;

    public ClayInterface(String iconstr, Class<? extends TileClayContainerInterface> tileEntityClass, int tier) {
        super(Material.iron, (Class) tileEntityClass, 0, tier);
        this.iconstr = iconstr;
        setStepSound(Block.soundTypeMetal);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 0);
    }


    protected boolean onBlockRightClicked(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        world.markBlockForUpdate(x, y, z);
        TileEntity te = UtilBuilder.safeGetTileEntity((IBlockAccess) world, x, y, z);
        if (te instanceof TileClayContainerInterface) {
            TileClayContainerInterface ti = (TileClayContainerInterface) te;
            if (ti.isSynced()) {


                openGui(99, world, x, y, z, player);
                return true;
            }
        }
        return false;
    }

    public void registerBlockIcons(IIconRegister par1IconRegister) {
        super.registerBlockIcons(par1IconRegister);


        setSameOverlayIcons(par1IconRegister.registerIcon("clayium:interface"));
    }


    @SideOnly(Side.CLIENT)
    public void registerIOIcons(IIconRegister par1IconRegister) {
        this.DefaultInsertIcon = par1IconRegister.registerIcon("clayium:import");
        this.DefaultExtractIcon = par1IconRegister.registerIcon("clayium:export");
        this.DefaultInsertPipeIcon = par1IconRegister.registerIcon("clayium:import_p");
        this.DefaultExtractPipeIcon = par1IconRegister.registerIcon("clayium:export_p");
    }


    @SideOnly(Side.CLIENT)
    public IIcon[] getInsertIcons(IBlockAccess world, int x, int y, int z) {
        nestCounter++;
        TileClayContainerInterface te = (TileClayContainerInterface) UtilBuilder.safeGetTileEntity(world, x, y, z);
        if (!te.isSynced() || nestCounter >= 10) {
            nestCounter--;
            return this.InsertIcons;
        }
        Block block = (te.getCoreWorld() == null) ? null : te.getCoreWorld().getBlock(te.getCoreBlockXCoord() + x, te
                .getCoreBlockYCoord() + y, te.getCoreBlockZCoord() + z);
        if (!(block instanceof ClayContainer)) {
            nestCounter--;
            return new IIcon[] {this.DefaultInsertIcon, this.DefaultInsertIcon, this.DefaultInsertIcon, this.DefaultInsertIcon};
        }
        IIcon[] iicons = ((ClayContainer) block).getInsertIcons((IBlockAccess) te.getWorldObj(), te
                .getCoreBlockXCoord() + x, te
                .getCoreBlockYCoord() + y, te.getCoreBlockZCoord() + z);
        nestCounter--;

        return iicons;
    }

    @SideOnly(Side.CLIENT)
    public IIcon[] getExtractIcons(IBlockAccess world, int x, int y, int z) {
        nestCounter++;
        TileClayContainerInterface te = (TileClayContainerInterface) UtilBuilder.safeGetTileEntity(world, x, y, z);
        if (!te.isSynced() || nestCounter >= 10) {
            nestCounter--;
            return this.ExtractIcons;
        }
        Block block = (te.getCoreWorld() == null) ? null : te.getCoreWorld().getBlock(te.getCoreBlockXCoord() + x, te
                .getCoreBlockYCoord() + y, te.getCoreBlockZCoord() + z);
        if (!(block instanceof ClayContainer)) {
            nestCounter--;
            return new IIcon[] {this.DefaultExtractIcon, this.DefaultExtractIcon, this.DefaultExtractIcon, this.DefaultExtractIcon};
        }
        IIcon[] iicons = ((ClayContainer) block).getExtractIcons((IBlockAccess) te.getWorldObj(), te
                .getCoreBlockXCoord() + x, te
                .getCoreBlockYCoord() + y, te.getCoreBlockZCoord() + z);
        nestCounter--;
        return iicons;
    }

    @SideOnly(Side.CLIENT)
    public IIcon[] getInsertPipeIcons(IBlockAccess world, int x, int y, int z) {
        nestCounter++;
        TileClayContainerInterface te = (TileClayContainerInterface) UtilBuilder.safeGetTileEntity(world, x, y, z);
        if (!te.isSynced() || nestCounter >= 10) {
            nestCounter--;
            return this.InsertIcons;
        }
        Block block = (te.getCoreWorld() == null) ? null : te.getCoreWorld().getBlock(te.getCoreBlockXCoord() + x, te
                .getCoreBlockYCoord() + y, te.getCoreBlockZCoord() + z);
        if (!(block instanceof ClayContainer)) {
            nestCounter--;
            return new IIcon[] {this.DefaultInsertPipeIcon, this.DefaultInsertPipeIcon, this.DefaultInsertPipeIcon, this.DefaultInsertPipeIcon};
        }
        IIcon[] iicons = ((ClayContainer) block).getInsertPipeIcons((IBlockAccess) te.getWorldObj(), te
                .getCoreBlockXCoord() + x, te
                .getCoreBlockYCoord() + y, te.getCoreBlockZCoord() + z);
        nestCounter--;

        return iicons;
    }

    @SideOnly(Side.CLIENT)
    public IIcon[] getExtractPipeIcons(IBlockAccess world, int x, int y, int z) {
        nestCounter++;
        TileClayContainerInterface te = (TileClayContainerInterface) UtilBuilder.safeGetTileEntity(world, x, y, z);
        if (!te.isSynced() || nestCounter >= 10) {
            nestCounter--;
            return this.ExtractIcons;
        }
        Block block = (te.getCoreWorld() == null) ? null : te.getCoreWorld().getBlock(te.getCoreBlockXCoord() + x, te
                .getCoreBlockYCoord() + y, te.getCoreBlockZCoord() + z);
        if (!(block instanceof ClayContainer)) {
            nestCounter--;
            return new IIcon[] {this.DefaultExtractPipeIcon, this.DefaultExtractPipeIcon, this.DefaultExtractPipeIcon, this.DefaultExtractPipeIcon};
        }
        IIcon[] iicons = ((ClayContainer) block).getExtractPipeIcons((IBlockAccess) te.getWorldObj(), te
                .getCoreBlockXCoord() + x, te
                .getCoreBlockYCoord() + y, te.getCoreBlockZCoord() + z);
        nestCounter--;
        return iicons;
    }


    public List getTooltip(ItemStack itemStack) {
        List ret = UtilLocale.localizeTooltip("tooltip.Interface");
        ret.addAll(super.getTooltip(itemStack));
        return ret;
    }
}
