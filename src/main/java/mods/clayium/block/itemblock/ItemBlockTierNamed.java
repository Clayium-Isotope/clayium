package mods.clayium.block.itemblock;

import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockTierNamed extends ItemBlockTiered {
    protected final String baseKey;
    protected final Object[] formatArgs;

    public ItemBlockTierNamed(Block block, String baseKey, Object ...formatArgs) {
        super(block);
        this.baseKey = baseKey;
        this.formatArgs = formatArgs;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        final String defaultKey = this.getUnlocalizedNameInefficiently(stack) + ".name";
        if (UtilLocale.canLocalize(defaultKey)) {
            return UtilLocale.localizeAndFormat(defaultKey);
        }

        return UtilLocale.localizeAndFormat(this.baseKey, this.formatArgs);
    }
}
