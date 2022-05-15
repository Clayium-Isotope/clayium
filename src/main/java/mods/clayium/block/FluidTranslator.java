package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

import mods.clayium.block.tile.TileFluidTranslator;
import mods.clayium.util.UtilLocale;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;

public class FluidTranslator
        extends ClayBuffer {
    public FluidTranslator(int tier) {
        super(tier, (Class) TileFluidTranslator.class);
    }


    @SideOnly(Side.CLIENT)
    public void registerIOIcons(IIconRegister par1IconRegister) {
        registerInsertIcons(par1IconRegister, new String[] {"import_l", "import"});
        registerExtractIcons(par1IconRegister, new String[] {"export_l", "export"});
    }


    public List getTooltip(ItemStack itemStack) {
        List ret = UtilLocale.localizeTooltip("tooltip.FluidTranslator");

        return ret;
    }
}
