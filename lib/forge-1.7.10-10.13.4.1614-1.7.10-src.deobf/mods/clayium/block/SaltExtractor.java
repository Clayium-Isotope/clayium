package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

import mods.clayium.block.tile.TileSaltExtractor;
import mods.clayium.util.UtilLocale;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;

public class SaltExtractor
        extends ClayNoRecipeMachines {
    public SaltExtractor(int tier) {
        super((String) null, "clayium:saltextractor", tier, (Class) TileSaltExtractor.class, 2);
        this.guiId = 11;
    }


    @SideOnly(Side.CLIENT)
    public void registerIOIcons(IIconRegister par1IconRegister) {
        registerInsertIcons(par1IconRegister, new String[] {"import_energy"});
        registerExtractIcons(par1IconRegister, new String[] {"export"});
    }


    public List getTooltip(ItemStack itemStack) {
        List ret = UtilLocale.localizeTooltip("tooltip.SaltExtractor");
        ret.addAll(super.getTooltip(itemStack));
        return ret;
    }
}
