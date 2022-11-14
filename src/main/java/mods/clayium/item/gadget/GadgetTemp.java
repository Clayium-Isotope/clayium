package mods.clayium.item.gadget;

import mods.clayium.item.common.ItemTiered;

public abstract class GadgetTemp extends ItemTiered implements IGadget {
    private final int meta;

    public GadgetTemp(String modelPath, int meta, int tier) {
        super(modelPath, tier);

        this.meta = meta;
        setMaxStackSize(1);
    }

    @Override
    public int getMeta() {
        return meta;
    }
}
