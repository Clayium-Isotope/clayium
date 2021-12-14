package mods.clayium.block;

import mods.clayium.block.common.BlockDamaged;
import mods.clayium.block.common.ClayiumBlock;
import mods.clayium.core.ClayiumCore;
import mods.clayium.machine.ClayBendingMachine.ClayBendingMachine;
import mods.clayium.machine.ClayCraftingTable.ClayCraftingTable;
import mods.clayium.machine.ClayWorkTable.ClayWorkTable;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
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
                if (field.get(instance) instanceof BlockDamaged) {
                    ClayiumCore.logger.info("ItemDamaged!");
                    for (Block _block : (BlockDamaged) field.get(instance)) {
                        ClayiumCore.logger.info(_block.getRegistryName());
                        if (_block instanceof ClayiumBlock) {
                            blocks.add(_block);
                            items.add(_block.getItemDropped(_block.getDefaultState(), new Random(), 0).setRegistryName(_block.getRegistryName()));
                        }
                    };
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
    public static final BlockDamaged compressedClays = new BlockDamaged() {
        public void init() {
            add(Blocks.CLAY);
            for (int i = 0; i < 13; i++) {
                add(new CompressedClay(i));
            }
        }
    };
    /* ...Compressed Clays */

    /* Machine Hulls... */
    public static final Block rawClayMachineHull = new RawClayMachineHull();

    public static final BlockDamaged machineHulls = new BlockDamaged() {
        public void init() {
            for (int i = 0; i < 13; i++) {
                add(new MachineHull(i));
            }
        }
    };

    public static final Block alloyHullAZ91D = new AZ91DAlloyHull();
    public static final Block alloyHullZK60A = new ZK60AAlloyHull();
    /* ...Machine Hulls */

    /* Machines... */
    /* Tier 0... */
    public static final Block clayWorkTable = new ClayWorkTable();
    public static final Block clayCraftingTable = new ClayCraftingTable();
    /* ...Tier 0 */

    /* Tier 1... */
    public static final Block clayBendingMachine = new ClayBendingMachine();
    /* ...Tier 1 */
    /* ...Machines */
    /* ...Elements */

    private static final List<Block> blocks = new ArrayList<>();
    private static final List<Item> items = new ArrayList<>();
}
