package mods.clayium.block;

import java.util.List;

import mods.clayium.block.tile.TileClayBuffer;
import mods.clayium.util.UtilLocale;
import net.minecraft.item.ItemStack;

public class ClayBuffer
        extends ClayNoRecipeMachines {
    public ClayBuffer(int tier) {
        this(tier, TileClayBuffer.class);
    }

    public ClayBuffer(int tier, Class<? extends TileClayBuffer> tileClass) {
        super((String) null, "", tier, (Class) tileClass, 2);
        this.guiId = 11;
    }


    public List getTooltip(ItemStack itemStack) {
        List ret = UtilLocale.localizeTooltip("tooltip.Buffer");
        ret.addAll(super.getTooltip(itemStack));
        return ret;
    }
}
