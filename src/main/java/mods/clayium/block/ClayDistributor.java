package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

import mods.clayium.block.tile.TileClayDistributor;
import mods.clayium.util.UtilLocale;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;

public class ClayDistributor
        extends ClayNoRecipeMachines {
    public ClayDistributor(int tier) {
        super((String) null, "", tier, (Class) TileClayDistributor.class, 2);
        this.guiId = 14;
    }


    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        super.registerBlockIcons(par1IconRegister);


        setSameOverlayIcons(par1IconRegister.registerIcon("clayium:distributor"));
    }


    public List getTooltip(ItemStack itemStack) {
        List ret = UtilLocale.localizeTooltip("tooltip.Distributor");
        ret.addAll(super.getTooltip(itemStack));
        return ret;
    }
}
