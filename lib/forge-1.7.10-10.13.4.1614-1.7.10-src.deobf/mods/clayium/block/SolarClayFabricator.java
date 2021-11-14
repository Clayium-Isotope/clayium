package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.clayium.block.tile.TileSolarClayFabricator;
import net.minecraft.client.renderer.texture.IIconRegister;

public class SolarClayFabricator
        extends ClayNoRecipeMachines {
    public SolarClayFabricator(String guititle, int tier) {
        super(guititle, "", tier, (Class) TileSolarClayFabricator.class, 2);

        this.guiId = 1;
    }


    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        super.registerBlockIcons(par1IconRegister);


        this.UpOverlayIcon = par1IconRegister.registerIcon("clayium:solar");
    }


    @SideOnly(Side.CLIENT)
    public void registerIOIcons(IIconRegister par1IconRegister) {
        registerInsertIcons(par1IconRegister, new String[] {"import"});
        registerExtractIcons(par1IconRegister, new String[] {"export"});
    }
}
