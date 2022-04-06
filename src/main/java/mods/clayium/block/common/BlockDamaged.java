package mods.clayium.block.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.Random;

public class BlockDamaged extends ArrayList<Block> {
    public BlockDamaged() {
        super();
        init();
    }

    public void init() {}

    public boolean contains(Item item) {
        Random rand = new Random();
        for (Block block : this) {
            if (Item.getItemFromBlock(block).equals(item)) return true;
//            if (block instanceof ClayiumBlock && ((ClayiumBlock) block).getItemBlock().equals(item)) return true;
            if (block.getItemDropped(block.getDefaultState(), rand, 0) == item) return true;
        }
        return false;
    }
}
