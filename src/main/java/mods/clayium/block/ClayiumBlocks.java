package mods.clayium.block;

import mods.clayium.machines.ClayWorkTable.ClayWorkTable;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClayiumBlocks {
    public static void initBlocks() {
        blocks.clear();
        items.clear();

        try {
            for (Field field : ClayiumBlocks.class.getFields()) {
                if (field.get(instance) instanceof Block) {
                    Block block = (Block) field.get(instance);
                    blocks.add(block);
                    items.add(block.getItemDropped(block.getDefaultState(), new Random(), 0).setRegistryName(block.getRegistryName()));
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
    /* Ores... */
    public static final Block clayOre = new ClayOre();
    public static final Block denseClayOre = new DenseClayOre();
    public static final Block largeDenseClayOre = new LargeDenseClayOre();
    /* ...Ores */

    /* Compressed Clays... */
    public static final Block compressedClay0 = new CompressedClay(0);
    public static final Block compressedClay1 = new CompressedClay(1);
    public static final Block compressedClay2 = new CompressedClay(2);
    public static final Block compressedClay3 = new CompressedClay(3);
    public static final Block compressedClay4 = new CompressedClay(4);
    public static final Block compressedClay5 = new CompressedClay(5);
    public static final Block compressedClay6 = new CompressedClay(6);
    public static final Block compressedClay7 = new CompressedClay(7);
    public static final Block compressedClay8 = new CompressedClay(8);
    public static final Block compressedClay9 = new CompressedClay(9);
    public static final Block compressedClay10 = new CompressedClay(10);
    public static final Block compressedClay11 = new CompressedClay(11);
    public static final Block compressedClay12 = new CompressedClay(12);
    /* ...Compressed Clays */

    /* Machine Hulls... */
    public static final Block rawClayMachineHull = new RawClayMachineHull();

    public static final Block machineHull0 = new MachineHull(0);
    public static final Block machineHull1 = new MachineHull(1);
    public static final Block machineHull2 = new MachineHull(2);
    public static final Block machineHull3 = new MachineHull(3);
    public static final Block machineHull4 = new MachineHull(4);
    public static final Block machineHull5 = new MachineHull(5);
    public static final Block machineHull6 = new MachineHull(6);
    public static final Block machineHull7 = new MachineHull(7);
    public static final Block machineHull8 = new MachineHull(8);
    public static final Block machineHull9 = new MachineHull(9);
    public static final Block machineHull10 = new MachineHull(10);
    public static final Block machineHull11 = new MachineHull(11);
    public static final Block machineHull12 = new MachineHull(12);
    /* ...Machine Hulls */

    /* Machines... */
    public static final Block clayWorkTable = new ClayWorkTable(0);
    /* ...Machines */
    /* ...Elements */

    private static final List<Block> blocks = new ArrayList<>();
    private static final List<Item> items = new ArrayList<>();
}
