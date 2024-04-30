package mods.clayium.item.common;

import java.util.ArrayList;

import net.minecraft.item.Item;

import mods.clayium.util.TierPrefix;

public class ItemDamaged extends ArrayList<Item> {

    public ItemDamaged(String modelPath, TierPrefix[] tiers) {
        super();
        for (TierPrefix i : tiers) {
            add(new ItemTiered(modelPath, i, this.size()));
        }
    }
}
