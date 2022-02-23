package mods.clayium.block;

import mods.clayium.block.common.BlockDamaged;
import mods.clayium.block.common.BlockTiered;
import mods.clayium.block.common.ClayiumBlock;
import mods.clayium.machine.ClayiumMachines;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.TierPrefix;
import mods.clayium.machine.common.ClayMachineTempTiered;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
                    for (Block _block : (BlockDamaged) field.get(instance)) {
                        if (_block instanceof ClayiumBlock) {
                            blocks.add(_block);
                            items.add(_block.getItemDropped(_block.getDefaultState(), new Random(), 0).setRegistryName(_block.getRegistryName()));
                        }
                    }
                }
            }
        } catch (IllegalAccessException ignore) {}

        for (Map.Entry<EnumMachineKind, Map<TierPrefix, ClayMachineTempTiered>> kinds : ClayiumMachines.machineMap.entrySet()) {
            for (Map.Entry<TierPrefix, ClayMachineTempTiered> tiers : kinds.getValue().entrySet()) {
                blocks.add(tiers.getValue());
                items.add(tiers.getValue().getItemDropped(tiers.getValue().getDefaultState(), new Random(), 0).setRegistryName(tiers.getValue().getRegistryName()));
            }
        }
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

    public static final BlockDamaged compressedClays = new BlockDamaged() {{
        add(Blocks.CLAY);
        for (int i = 0; i < 13; i++) {
            add(new CompressedClay(i));
        }
    }};

    public static final BlockDamaged CAReactorHull = new BlockDamaged() {{
        int[] tiers = { 10, 11, 11, 11, 11, 12, 12, 12, 12, 13 };
        for (int i = 0; i < 10; i++) {
            add(new BlockTiered(Material.IRON, "ca_reactor_hull_", i, tiers[i]) {{
                setSoundType(SoundType.METAL);
                setHarvestLevel("pickaxe", 0);
                setHardness(4.0F);
                setResistance(25.0F);
            }});
        }
    }};

    /* Machine Hulls... */
    public static final Block rawClayMachineHull = new RawClayMachineHull();

    public static final BlockDamaged machineHulls = new BlockDamaged() {{
        for (int i = 0; i < 13; i++) {
            add(new BlockTiered(Material.IRON, "machine_hull_", i, i + 1) {{
                setSoundType(SoundType.METAL);
                setHarvestLevel("pickaxe", 0);
                setHardness(2F);
                setResistance(5F);
            }});
        }
    }};

    public static final Block AZ91DAlloyHull = new AZ91DAlloyHull();
    public static final Block ZK60AAlloyHull = new ZK60AAlloyHull();
    /* ...Machine Hulls */
    /* ...Elements */

    private static final List<Block> blocks = new ArrayList<>();
    private static final List<Item> items = new ArrayList<>();
}
