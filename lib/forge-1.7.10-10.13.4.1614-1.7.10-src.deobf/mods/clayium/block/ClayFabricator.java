package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.clayium.block.tile.TileClayFabricator;
import net.minecraft.client.renderer.texture.IIconRegister;

public class ClayFabricator
        extends ClayNoRecipeMachines {
    public ClayFabricator(int tier) {
        super((String) null, "clayium:clayfabricator", tier, (Class) TileClayFabricator.class, 1);
        this.guiId = 1;
    }


    @SideOnly(Side.CLIENT)
    public void registerIOIcons(IIconRegister par1IconRegister) {
        registerInsertIcons(par1IconRegister, new String[] {"import"});
        registerExtractIcons(par1IconRegister, new String[] {"export"});
    }
}
