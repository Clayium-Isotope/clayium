package mods.clayium.item.gadget;

import mods.clayium.core.ClayiumCore;
import mods.clayium.util.TierPrefix;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public class GadgetOverclock extends GadgetTemp {
    private final int meta;

    public GadgetOverclock(int meta, int tier) {
        super("gadget_" + TierPrefix.get(tier).getPrefix() + "_overclock", meta, tier);
        this.meta = meta;
    }

    @Override
    public String getGroupName() {
        return "GadgetOverclock";
    }

    @Override
    public void onApply(Entity entity, ItemStack gadget, boolean isRemote) {}

    @Override
    public void onTick(Entity entity, ItemStack gadget, boolean isRemote) {
        if (isRemote && ClayiumCore.proxy.getClientPlayer() == entity) {
            ClayiumCore.proxy.overclockPlayer(3 - this.meta);
        }
    }

    @Override
    public void onReform(Entity entity, ItemStack before, ItemStack after, boolean isRemote) {}

    @Override
    public void onRemove(Entity entity, ItemStack gadget, boolean isRemote) {}
}
