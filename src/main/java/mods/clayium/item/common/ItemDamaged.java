package mods.clayium.item.common;

import mods.clayium.util.TierPrefix;
import net.minecraft.item.Item;

import java.util.ArrayList;

public class ItemDamaged extends ArrayList<Item> {

    public ItemDamaged(String modelPath, TierPrefix[] tiers) {
        super();
        for (TierPrefix i : tiers) {
            add(new ItemTiered(modelPath, i, this.size()));
        }
    }
}
