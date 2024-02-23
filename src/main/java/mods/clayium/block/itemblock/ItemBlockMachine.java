package mods.clayium.block.itemblock;

import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemBlockMachine extends ItemBlockTiered {
    protected final EnumMachineKind kind;
    protected final TierPrefix tier;

    public ItemBlockMachine(EnumMachineKind kind, TierPrefix tier, Block block) {
        super(block);
        this.kind = kind;
        this.tier = tier;
        assert block.getRegistryName() != null;
        this.setRegistryName(block.getRegistryName());
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        final String defaultKey = this.getUnlocalizedNameInefficiently(stack) + ".name";
        if (UtilLocale.canLocalize(defaultKey)) {
            return UtilLocale.localizeAndFormat(defaultKey);
        }

        if (!UtilLocale.canLocalize("util.display.tiered_machine")) {
            return "[util.display.tiered_machine]";
        }

        return UtilLocale.localizeAndFormat("util.display.tiered_machine",
                UtilLocale.localizeAndFormat(TierPrefix.getLocalizeKey(this.tier)),
                UtilLocale.localizeAndFormat(EnumMachineKind.getLocalizeKey(this.kind))
                );
    }
}
