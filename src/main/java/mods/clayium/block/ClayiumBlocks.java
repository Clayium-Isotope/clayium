package mods.clayium.block;

import mods.clayium.block.common.BlockDamaged;
import mods.clayium.block.common.ClayiumBlock;
import mods.clayium.core.ClayiumCore;
import mods.clayium.machine.ClayCraftingTable.ClayCraftingTable;
import mods.clayium.machine.ClayWorkTable.ClayWorkTable;
import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
import mods.clayium.machine.common.ClayMachineTempTiered;
import mods.clayium.machine.crafting.ClayiumRecipe;
import mods.clayium.machine.crafting.ClayiumRecipes;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

import java.lang.reflect.Field;
import java.util.*;

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
                    for (Block _block : (BlockDamaged) field.get(instance)) {
                        if (_block instanceof ClayiumBlock) {
                            blocks.add(_block);
                            items.add(_block.getItemDropped(_block.getDefaultState(), new Random(), 0).setRegistryName(_block.getRegistryName()));
                        }
                    }
                }
                if (field.get(instance) == machineMap) {
                    for (Map.Entry<ClayiumBlocks.MachineKind, Map<ClayiumBlocks.TierPrefix, ClayMachineTempTiered>> kinds : ClayiumBlocks.machineMap.entrySet()) {
                        for (Map.Entry<ClayiumBlocks.TierPrefix, ClayMachineTempTiered> tiers : kinds.getValue().entrySet()) {
                            blocks.add(tiers.getValue());
                            items.add(tiers.getValue().getItemDropped(tiers.getValue().getDefaultState(), new Random(), 0).setRegistryName(tiers.getValue().getRegistryName()));
                        }
                    }
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
    public enum TierPrefix {
        none(""),
        clay("clay"),
        denseClay("dense_clay"),
        simple("simple"),
        basic("basic"),
        advanced("advanced"),
        precision("precision"),
        claySteel("clay_steel"),
        clayium("clayium"),
        ultimate("ultimate"),
        antimatter("antimatter"),
        pureAntimatter("pure_antimatter"),
        OEC("oec"),
        OPA("opa");

        TierPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String get() {
            return prefix;
        }

        public static TierPrefix get(int tier) {
            switch (tier) {
                case 0: return none;
                case 1: return clay;
                case 2: return denseClay;
                case 3: return simple;
                case 4: return basic;
                case 5: return advanced;
                case 6: return precision;
                case 7: return claySteel;
                case 8: return clayium;
                case 9: return ultimate;
                case 10: return antimatter;
                case 11: return pureAntimatter;
                case 12: return OEC;
                case 13: return OPA;
            }

            ClayiumCore.logger.error(new IllegalAccessException());
            return null;
        }

        private final String prefix;
    }

    public enum MachineKind {
        workTable("work_table", ClayiumRecipes.clayWorkTable),
        craftingTable("crafting_table", null),
        bendingMachine("bending_machine", ClayiumRecipes.bendingMachine),
        wireDrawingMachine("wire_drawing_machine", ClayiumRecipes.wireDrawingMachine),
        pipeDrawingMachine("pipe_drawing_machine", ClayiumRecipes.pipeDrawingMachine),
        cuttingMachine("cutting_machine", ClayiumRecipes.cuttingMachine),
        lathe("lathe", ClayiumRecipes.lathe),
        ;

        MachineKind(String kind, ClayiumRecipe recipe) {
            this.kind = kind;
            this.recipe = recipe;
        }

        public String get() {
            return kind;
        }
        public ClayiumRecipe getRecipe() {
            return recipe;
        }

        private final String kind;
        private final ClayiumRecipe recipe;
    }

    public static final Map<MachineKind, Map<TierPrefix, ClayMachineTempTiered>> machineMap = new HashMap<>();
    static {
        for (MachineKind kind : MachineKind.values()) {
            machineMap.put(kind, new HashMap<>());
        }

        /* Tier 0... */
        machineMap.get(MachineKind.workTable).put(TierPrefix.none, new ClayWorkTable());
        machineMap.get(MachineKind.craftingTable).put(TierPrefix.none, new ClayCraftingTable());
        /* ...Tier 0 */

        /* Tier 1... */
        for (MachineKind kind : new MachineKind[] {
                MachineKind.bendingMachine,
                MachineKind.wireDrawingMachine,
                MachineKind.pipeDrawingMachine,
                MachineKind.cuttingMachine,
                MachineKind.lathe
        }) {
            machineMap.get(kind).put(TierPrefix.clay, new ClayiumMachine(kind, "", 1));
        }
        /* ...Tier 1 */
    }
    public static Block get(MachineKind kind, TierPrefix tier) {
        return machineMap.get(kind).get(tier);
    }
    public static Block get(MachineKind kind, int tier) {
        return get(kind, TierPrefix.get(tier));
    }
    /* ...Machines */
    /* ...Elements */

    private static final List<Block> blocks = new ArrayList<>();
    private static final List<Item> items = new ArrayList<>();
}
