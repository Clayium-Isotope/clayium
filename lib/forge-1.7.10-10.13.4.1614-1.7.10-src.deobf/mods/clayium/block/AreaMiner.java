package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.clayium.block.tile.TileAreaMiner;
import mods.clayium.block.tile.TileClayContainer;
import mods.clayium.util.UtilBuilder;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class AreaMiner
        extends ClayNoRecipeMachines {
    private IIcon[] FrontOverlayIcons;

    public AreaMiner(int tier, String iconstr) {
        this(tier, iconstr, (Class) TileAreaMiner.class, 16);
    }

    public AreaMiner(int tier, String iconstr, Class<? extends TileClayContainer> clazz, int guiId) {
        this((String) null, iconstr, (tier < 7) ? "clayium:az91dhull" : "clayium:zk60ahull", tier, clazz, (tier < 7) ? 2 : 1);
        this.guiId = guiId;
    }

    public AreaMiner(String guititle, String iconstr, String machineIconStr, int tier, Class<? extends TileClayContainer> tileEntityClass, int metaMode) {
        super(guititle, iconstr, machineIconStr, tier, tileEntityClass, 1, metaMode);
    }


    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        super.registerBlockIcons(par1IconRegister);
        this.FrontOverlayIcons = new IIcon[] {this.FrontOverlayIcon, par1IconRegister.registerIcon(this.iconstr + "_1")};
        if (this.tier < 7) {
            this.BackOverlayIcon = par1IconRegister.registerIcon("clayium:minerback");
        }
    }


    @SideOnly(Side.CLIENT)
    public IIcon getOverlayIcon(IBlockAccess world, int x, int y, int z, int side) {
        if (UtilBuilder.safeGetTileEntity(world, x, y, z) instanceof TileAreaMiner) {
            TileAreaMiner te = (TileAreaMiner) UtilBuilder.safeGetTileEntity(world, x, y, z);
            this.FrontOverlayIcon = this.FrontOverlayIcons[te.isDoingWork() ? 1 : 0];
        }
        IIcon iicon = super.getOverlayIcon(world, x, y, z, side);
        this.FrontOverlayIcon = this.FrontOverlayIcons[0];
        return iicon;
    }


    @SideOnly(Side.CLIENT)
    public void registerIOIcons(IIconRegister par1IconRegister) {
        registerInsertIcons(par1IconRegister, new String[] {"import_energy", "import"});
        registerExtractIcons(par1IconRegister, new String[] {"export"});
    }
}
