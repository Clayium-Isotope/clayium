package mods.clayium.block;

import mods.clayium.block.common.BlockDamaged;
import mods.clayium.block.common.BlockTierTied;
import mods.clayium.block.common.BlockTiered;
import mods.clayium.block.common.MaterialBlock;
import mods.clayium.item.common.ClayiumMaterial;
import mods.clayium.util.TierPrefix;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;

import java.util.ArrayList;
import java.util.List;

public class ClayiumBlocks {
    public static void registerBlocks() {
        blocks.add(clayOre);
        blocks.add(denseClayOre);
        blocks.add(largeDenseClayOre);

        for (int i = 0; i < 13; i++) {
            compressedClay.add(new CompressedClay(i));
        }
        blocks.addAll(compressedClay);

        blocks.add(rawClayMachineHull);

        for (int i = 0; i < 13; i++) {
            machineHulls.add(new BlockTiered(Material.IRON, "machine_hull_", i, i + 1) {{
                setSoundType(SoundType.METAL);
                setHarvestLevel("pickaxe", 0);
                setHardness(2F);
                setResistance(5F);
            }});
        }
        blocks.addAll(machineHulls);

        blocks.add(AZ91DHull);
        blocks.add(ZK60AHull);


        resonator.put(TierPrefix.antimatter, new Resonator(0, 10, 1.08D));
        resonator.put(TierPrefix.pureAntimatter, new Resonator(1, 11, 1.1D));
        resonator.put(TierPrefix.OEC, new Resonator(2, 12, 2.0D));
        resonator.put(TierPrefix.OPA, new Resonator(3, 13, 20.0D));
        blocks.addAll(resonator.entryList());

        overclocker.put(TierPrefix.antimatter, new Overclocker(0, 10, 1.5D));
        overclocker.put(TierPrefix.pureAntimatter, new Overclocker(1, 11, 2.3D));
        overclocker.put(TierPrefix.OEC, new Overclocker(2, 12, 3.5D));
        overclocker.put(TierPrefix.OPA, new Overclocker(3, 13, 5.5D));
        blocks.addAll(overclocker.entryList());

        energyStorageUpgrade.put(TierPrefix.antimatter, new EnergyStorageUpgrade(0, 10, 1));
        energyStorageUpgrade.put(TierPrefix.pureAntimatter, new EnergyStorageUpgrade(1, 11, 3));
        energyStorageUpgrade.put(TierPrefix.OEC, new EnergyStorageUpgrade(2, 12, 7));
        energyStorageUpgrade.put(TierPrefix.OPA, new EnergyStorageUpgrade(3, 13, 63));
        blocks.addAll(energyStorageUpgrade.entryList());

        CAReactorCoil.put(TierPrefix.antimatter, new CAReactorCoil(0, 10));
        CAReactorCoil.put(TierPrefix.pureAntimatter, new CAReactorCoil(1, 11));
        CAReactorCoil.put(TierPrefix.OEC, new CAReactorCoil(2, 12));
        CAReactorCoil.put(TierPrefix.OPA, new CAReactorCoil(3, 13));
        blocks.addAll(CAReactorCoil.entryList());

        int[] tiers = { 10, 11, 11, 11, 11, 12, 12, 12, 12, 13 };
        for (int i = 0; i < 10; i++) {
            CAReactorHull.add(new BlockTiered(Material.IRON, "ca_reactor_hull_", i, tiers[i]) {{
                setSoundType(SoundType.METAL);
                setHarvestLevel("pickaxe", 0);
                setHardness(4.0F);
                setResistance(25.0F);
            }});
        }
        blocks.addAll(CAReactorHull);

        materialBlock.add(new MaterialBlock(ClayiumMaterial.impureSilicon, 0, 5));
        materialBlock.add(new MaterialBlock(ClayiumMaterial.silicone, 1, 5));
        materialBlock.add(new MaterialBlock(ClayiumMaterial.silicon, 2, 5));
        materialBlock.add(new MaterialBlock(ClayiumMaterial.aluminium, 3, 6));
        materialBlock.add(new MaterialBlock(ClayiumMaterial.claySteel, 4, 7));
        materialBlock.add(new MaterialBlock(ClayiumMaterial.clayium, 5, 8));
        materialBlock.add(new MaterialBlock(ClayiumMaterial.ultimateAlloy, 6, 9));
        materialBlock.add(new MaterialBlock(ClayiumMaterial.antimatter, 7, 10));
        materialBlock.add(new MaterialBlock(ClayiumMaterial.pureAntimatter, 8, 11));
        materialBlock.add(new MaterialBlock(ClayiumMaterial.octupleEnergeticClay, 9, 12));
        materialBlock.add(new MaterialBlock(ClayiumMaterial.octuplePureAntimatter, 10, 13));
        blocks.addAll(materialBlock);

        for (EnumDyeColor color : EnumDyeColor.values()) {
            siliconeColored.add(new SiliconeColored(color));
        }
        blocks.addAll(siliconeColored);
    }

    public static List<Block> getBlocks() {
        return blocks;
    }

    /* Elements... */
    /* Ores... */
    public static final Block clayOre = new ClayOre();
    public static final Block denseClayOre = new DenseClayOre();
    public static final Block largeDenseClayOre = new LargeDenseClayOre();
    /* ...Ores */

    public static final BlockDamaged compressedClay = new BlockDamaged();

    /* Machine Hulls... */
    public static final Block rawClayMachineHull = new RawClayMachineHull();

    public static final BlockDamaged machineHulls = new BlockDamaged();

    public static final Block AZ91DHull = new AZ91DHull();
    public static final Block ZK60AHull = new ZK60AHull();
    /* ...Machine Hulls */
    /* ...Elements */

    public static final BlockTierTied resonator = new BlockTierTied();
    public static final BlockTierTied overclocker = new BlockTierTied();
    public static final BlockTierTied energyStorageUpgrade = new BlockTierTied();
    public static final BlockTierTied CAReactorCoil = new BlockTierTied();
    public static final BlockDamaged CAReactorHull = new BlockDamaged();
    public static final BlockDamaged materialBlock = new BlockDamaged();
    public static final BlockDamaged siliconeColored = new BlockDamaged();

    public static final Block clayTreeSapling = Blocks.AIR;

    private static final List<Block> blocks = new ArrayList<>();
}
