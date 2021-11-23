package mods.clayium.item.common;

import net.minecraft.item.Item;

import java.util.ArrayList;

public class ItemDamaged extends ArrayList<Item> {
    public ItemDamaged(String modelPath, int[] metas) {
        super();
        for (int i : metas) {
            add(new ItemTiered(modelPath, i));
        }
    }
}
