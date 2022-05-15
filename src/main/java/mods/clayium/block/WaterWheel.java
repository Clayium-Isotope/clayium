package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

import mods.clayium.block.tile.TileWaterWheel;
import mods.clayium.util.UtilBuilder;
import mods.clayium.util.UtilLocale;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class WaterWheel
        extends ClayNoRecipeMachines {
    @SideOnly(Side.CLIENT)
    public IIcon[] FrontOverlayIcons;

    public WaterWheel(String guititle, String iconstr, int tier) {
        super(guititle, iconstr, tier, (Class) TileWaterWheel.class, 2);
        this.guiId = 10;
    }


    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        super.registerBlockIcons(par1IconRegister);
        this.FrontOverlayIcons = new IIcon[] {this.FrontOverlayIcon, par1IconRegister.registerIcon(this.iconstr + "_1")};
    }


    @SideOnly(Side.CLIENT)
    public IIcon getOverlayIcon(IBlockAccess world, int x, int y, int z, int side) {
        TileWaterWheel te = (TileWaterWheel) UtilBuilder.safeGetTileEntity(world, x, y, z);
        this.FrontOverlayIcon = this.FrontOverlayIcons[te.getProgressIcon()];
        IIcon iicon = super.getOverlayIcon(world, x, y, z, side);
        this.FrontOverlayIcon = this.FrontOverlayIcons[0];
        return iicon;
    }


    public List getTooltip(ItemStack itemStack) {
        List ret = UtilLocale.localizeTooltip("tooltip.WaterWheel");
        ret.addAll(super.getTooltip(itemStack));
        return ret;
    }
}
