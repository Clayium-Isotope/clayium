package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.clayium.block.tile.TileAutoCrafter;
import net.minecraft.client.renderer.texture.IIconRegister;

public class ClayAutoCrafter
        extends ClayNoRecipeMachines {
    public ClayAutoCrafter(int tier) {
        super("", "", tier, (Class) TileAutoCrafter.class, 1);
        this.guiId = 17;
    }


    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        super.registerBlockIcons(par1IconRegister);
        this.UpOverlayIcon = par1IconRegister.registerIcon("clayium:autocraftertop");
        this.FrontOverlayIcon = this.BackOverlayIcon = this.RightOverlayIcon = this.LeftOverlayIcon = par1IconRegister.registerIcon("clayium:autocrafterside");
    }
}
