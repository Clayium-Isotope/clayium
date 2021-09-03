package mods.clayium.block;

import mods.clayium.machines.ClayWorkTable.ClayWorkTable;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ClayiumBlocks {
    public ClayiumBlocks() {}

    public static void initBlocks() {
        blocks.clear();
        items.clear();

        try {
            for (Field field : ClayiumBlocks.class.getFields()) {
                if (field.get(instance) instanceof Block) {
                    Block block = (Block) field.get(instance);
                    blocks.add(block);
                    items.add(new ItemBlock(block).setRegistryName(block.getRegistryName()));
                }
            }
        } catch (IllegalAccessException ignore) {}
    }

    public static List<Block> getBlocks() {
        return blocks;
    }

    public static List<Item> getItems() {
        return items;
    }

    private static final ClayiumBlocks instance = new ClayiumBlocks();

    /* Elements... */
    /* Machines... */
    public static final Block clayWorkTable = new ClayWorkTable(0);
    /* ...Machines */

    /* Ores... */
    public static final Block clayOre = new ClayOre();
    public static final Block denseClayOre = new DenseClayOre();
    public static final Block largeDenseClayOre = new LargeDenseClayOre();
    /* ...Ores */

    /* Compressed... */
    public static final Block compressedClay0 = new CompressedClay0();
    public static final Block compressedClay1 = new CompressedClay1();
    /* ...Compressed */
    /* ...Elements */

    private static final List<Block> blocks = new ArrayList<>();
    private static final List<Item> items = new ArrayList<>();
}
