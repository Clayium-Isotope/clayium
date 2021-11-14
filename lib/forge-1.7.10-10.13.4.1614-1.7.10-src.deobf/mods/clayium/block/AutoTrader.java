package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.clayium.block.tile.TileAutoTrader;
import net.minecraft.client.renderer.texture.IIconRegister;

public class AutoTrader
        extends ClayNoRecipeMachines {
    public AutoTrader(int tier) {
        super((String) null, (String) null, "clayium:az91dhull", tier, (Class) TileAutoTrader.class, 1);
        this.guiId = 19;
    }


    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        super.registerBlockIcons(par1IconRegister);
        this.UpOverlayIcon = par1IconRegister.registerIcon("clayium:autotradertop");
        this
                .FrontOverlayIcon = this.BackOverlayIcon = this.RightOverlayIcon = this.LeftOverlayIcon = par1IconRegister.registerIcon("clayium:autotraderside");
    }


    @SideOnly(Side.CLIENT)
    public void registerIOIcons(IIconRegister par1IconRegister) {
        registerInsertIcons(par1IconRegister, new String[] {"import_1", "import_2", "import", "import_energy"});
        registerExtractIcons(par1IconRegister, new String[] {"export"});
    }
}
