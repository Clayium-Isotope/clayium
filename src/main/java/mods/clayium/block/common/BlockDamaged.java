package mods.clayium.block.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class BlockDamaged extends ArrayList<Block> {

    protected static Random rand = new Random();

    public BlockDamaged() {
        super();
        init();
    }

    public void init() {}

    public boolean contains(Item item) {
        for (Block block : this) {
            if (Item.getItemFromBlock(block).equals(item)) return true;
            // if (block instanceof ClayiumBlock && ((ClayiumBlock) block).getItemBlock().equals(item)) return true;
            if (block.getItemDropped(block.getDefaultState(), rand, 0) == item) return true;
        }
        return false;
    }

    public boolean contains(Block block) {
        return super.contains(block);
    }

    public ItemStack get(int meta, int amount) {
        return new ItemStack(this.get(meta), amount/* TODO, meta */);
    }
}
