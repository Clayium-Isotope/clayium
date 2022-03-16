package mods.clayium.block;

import mods.clayium.block.common.BlockDamaged;
import mods.clayium.block.common.BlockTierTied;
import mods.clayium.block.common.BlockTiered;
import mods.clayium.block.common.ClayiumBlock;
import mods.clayium.machine.ClayiumMachines;
import mods.clayium.machine.TierPrefix;
import mods.clayium.machine.common.ClayMachineTempTiered;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;

import java.lang.reflect.Field;
import java.util.*;

public class ClayiumBlocks {
    public static void initBlocks() {
        blocks.clear();
        items.clear();

        try {
            for (Field field : ClayiumBlocks.class.getFields()) {
                if (field.get(instance) instanceof ClayiumBlock) {
                    ClayiumBlock block = (ClayiumBlock) field.get(instance);

                    blocks.add(block);
                    items.add(block.getItemBlock().setRegistryName(block.getRegistryName()));
                }
                if (field.get(instance) instanceof BlockDamaged) {
                    for (Block _block : (BlockDamaged) field.get(instance)) {
                        if (_block instanceof ClayiumBlock) {
                            blocks.add(_block);
                            items.add(((ClayiumBlock) _block).getItemBlock().setRegistryName(_block.getRegistryName()));
                        }
                    }
                }
                if (field.get(instance) instanceof BlockTierTied) {
                    for (Block _block : ((BlockTierTied) field.get(instance)).entryList()) {
                        if (_block instanceof ClayiumBlock) {
                            blocks.add(_block);
                            items.add(((ClayiumBlock) _block).getItemBlock().setRegistryName(_block.getRegistryName()));
                        }
                    }
                }
            }
        } catch (IllegalAccessException ignore) {}

        for (Map<TierPrefix, ClayMachineTempTiered> kinds : ClayiumMachines.machineMap.values()) {
            for (ClayMachineTempTiered machine : kinds.values()) {
                blocks.add(machine);
                items.add(machine.getItemBlock().setRegistryName(machine.getRegistryName()));
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

    public static final BlockDamaged siliconeColored = new BlockDamaged() {{
        for (EnumDyeColor color : EnumDyeColor.values()) {
            add(new SiliconeColored(color));
        }
    }};

    public static final BlockTierTied resonator = new BlockTierTied() {{
        put(TierPrefix.antimatter, new Resonator(10, 1.08D));
        put(TierPrefix.pureAntimatter, new Resonator(11, 1.1D));
        put(TierPrefix.OEC, new Resonator(12, 2.0D));
        put(TierPrefix.OPA, new Resonator(13, 20.0D));
    }};

    public static final BlockTierTied overclocker = new BlockTierTied() {{
        put(TierPrefix.antimatter, new Overclocker(10, 1.5D));
        put(TierPrefix.pureAntimatter, new Overclocker(11, 2.3D));
        put(TierPrefix.OEC, new Overclocker(12, 3.5D));
        put(TierPrefix.OPA, new Overclocker(13, 5.5D));
    }};

    public static final BlockTierTied energyStorageUpgrade = new BlockTierTied() {{
        put(TierPrefix.antimatter, new EnergyStorageUpgrade(10, 1));
        put(TierPrefix.pureAntimatter, new EnergyStorageUpgrade(11, 3));
        put(TierPrefix.OEC, new EnergyStorageUpgrade(12, 7));
        put(TierPrefix.OPA, new EnergyStorageUpgrade(13, 63));
    }};

    public static final BlockTierTied CAReactorCoil = new BlockTierTied() {{
        put(TierPrefix.antimatter, new CAReactorCoil(10));
        put(TierPrefix.pureAntimatter, new CAReactorCoil(11));
        put(TierPrefix.OEC, new CAReactorCoil(12));
        put(TierPrefix.OPA, new CAReactorCoil(13));
    }};

    private static final List<Block> blocks = new ArrayList<>();
    private static final List<Item> items = new ArrayList<>();
}
