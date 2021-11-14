package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

import mods.clayium.block.tile.TileMultitrackBuffer;
import mods.clayium.util.UtilLocale;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;

public class MultitrackBuffer
        extends ClayNoRecipeMachines {
    public MultitrackBuffer(int tier) {
        this(tier, TileMultitrackBuffer.class);
    }

    public MultitrackBuffer(int tier, Class<? extends TileMultitrackBuffer> tileClass) {
        super((String) null, "", tier, (Class) tileClass, 2);
        this.guiId = 31;
    }


    public List getTooltip(ItemStack itemStack) {
        List ret = UtilLocale.localizeTooltip("tooltip.MultitrackBuffer");
        ret.addAll(super.getTooltip(itemStack));
        return ret;
    }


    @SideOnly(Side.CLIENT)
    public void registerIOIcons(IIconRegister par1IconRegister) {
        registerInsertIcons(par1IconRegister, new String[] {"import_m0", "import_m1", "import_m2", "import_m3", "import_m4", "import_m5", "import_m6"});
        registerExtractIcons(par1IconRegister, new String[] {"export_m0", "export_m1", "export_m2", "export_m3", "export_m4", "export_m5", "export_m6"});
    }
}
